package com.admin.movieApi.service.Movie;

import com.admin.movieApi.dto.MovieDto;
import com.admin.movieApi.dto.MoviePageResponse;
import com.admin.movieApi.entities.Movie;
import com.admin.movieApi.exception.FileExistsException;
import com.admin.movieApi.exception.MovieNotFoundException;
import com.admin.movieApi.repository.MovieRepository;
import com.admin.movieApi.service.File.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    public final MovieRepository repository;
    public final FileService fileService;

    @Value("${project.poster}")
    private String path;


    public MovieServiceImpl(MovieRepository repository, FileService fileService) {
        this.repository = repository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        String posterUrl = fileService.uploadFile(path, file);

        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getCast(),
                movieDto.getReleaseYear(),
                posterUrl
        );
        
        Movie savedMovie = repository.save(movie);
        
        return new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                savedMovie.getPoster()
        );
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        Movie movie = repository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));
                
        return new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getCast(),
                movie.getReleaseYear(),
                movie.getPoster(),  // This is already the full S3 URL
                movie.getPoster()   // Return the same URL for both poster and posterUrl
        );
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = repository.findAll();
        List<MovieDto> dtos = new ArrayList<>();

        for (Movie movie : movies) {
            dtos.add(new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),  // Full S3 URL
                    movie.getPoster()   // Same URL for both fields
            ));
        }

        return dtos;
    }


    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        Movie existingMovie = repository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        if (file != null && !file.isEmpty()) {
            if (existingMovie.getPoster() != null && !existingMovie.getPoster().isEmpty()) {
                Files.deleteIfExists(Paths.get(path + File.separator + existingMovie.getPoster()));
            }
            String uploadedFileName = fileService.uploadFile(path, file);
            movieDto.setPoster(uploadedFileName);
        } else {
            movieDto.setPoster(existingMovie.getPoster());
        }

        existingMovie.setTitle(movieDto.getTitle());
        existingMovie.setDirector(movieDto.getDirector());
        existingMovie.setStudio(movieDto.getStudio());
        existingMovie.setCast(movieDto.getCast());
        existingMovie.setReleaseYear(movieDto.getReleaseYear());
        existingMovie.setPoster(movieDto.getPoster());

        Movie updatedMovie = repository.save(existingMovie);

        // The poster field already contains the full S3 URL from FileService
        String posterUrl = updatedMovie.getPoster();

        return new MovieDto(
                updatedMovie.getMovieId(),
                updatedMovie.getTitle(),
                updatedMovie.getDirector(),
                updatedMovie.getStudio(),
                updatedMovie.getCast(),
                updatedMovie.getReleaseYear(),
                updatedMovie.getPoster(),
                posterUrl
        );
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        Movie movie = repository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));
        repository.delete(movie);

        return "Movie deleted with id: " + movieId;
    }

    @Override
    public MoviePageResponse getAllMoviesByPagination(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePage = repository.findAll(page);
        List<Movie> movies = moviePage.getContent();
        List<MovieDto> dtos = new ArrayList<>();

        for (Movie movie : movies) {
            dtos.add(new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    movie.getPoster()  // Using the full S3 URL directly
            ));
        }
        return new MoviePageResponse(dtos, pageNumber, pageSize, moviePage.getTotalPages(), (int) moviePage.getTotalElements(), moviePage.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesByPaginationAndSort(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> moviePage = repository.findAll(page);
        List<Movie> movies = moviePage.getContent();
        List<MovieDto> dtos = new ArrayList<>();

        for (Movie movie : movies) {
            dtos.add(new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    movie.getPoster()  // Using the full S3 URL directly
            ));
        }
        return new MoviePageResponse(dtos, pageNumber, pageSize, moviePage.getTotalPages(), (int) moviePage.getTotalElements(), moviePage.isLast());
    }
}
