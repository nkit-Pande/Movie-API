package com.admin.movieApi.service.Movie;

import com.admin.movieApi.dto.MovieDto;
import com.admin.movieApi.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

    MovieDto getMovie(Integer movieId);

    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException;

    String deleteMovie(Integer movieId) throws IOException;

    MoviePageResponse getAllMoviesByPagination(Integer pageNumber, Integer pageSize);

    MoviePageResponse getAllMoviesByPaginationAndSort(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);

}
