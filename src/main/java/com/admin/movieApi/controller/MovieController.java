package com.admin.movieApi.controller;

import com.admin.movieApi.dto.MovieDto;
import com.admin.movieApi.dto.MoviePageResponse;
import com.admin.movieApi.exception.EmptyFileException;
import com.admin.movieApi.service.Movie.MovieService;
import com.admin.movieApi.util.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movie/")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Working";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MovieDto> addMovieHandler(
            @RequestPart(required = true) MultipartFile file,
            @RequestPart(required = true) String movieDto) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new EmptyFileException("Movie poster file is required");
        }
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
        MovieDto movie = movieService.getMovie(movieId);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMovie() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }


    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(
            @PathVariable Integer movieId,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart(required = true) String movieDto) throws IOException {

        MovieDto dto = convertToMovieDto(movieDto);
        return ResponseEntity.ok(movieService.updateMovie(movieId, dto, file));
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        String result = movieService.deleteMovie(movieId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/allMoviePage")
    public ResponseEntity<MoviePageResponse> getMovieWithPagination(
        @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ){
        return ResponseEntity.ok(movieService.getAllMoviesByPagination(pageNumber, pageSize));
    }

    @GetMapping("/allMoviePageAndSort")
    public ResponseEntity<MoviePageResponse> getMovieWithPaginationAndSort(
        @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
        @RequestParam(defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDirection
    ){
        return ResponseEntity.ok(movieService.getAllMoviesByPaginationAndSort(pageNumber, pageSize, sortBy, sortDirection));
    }



    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        if (movieDtoObj == null || movieDtoObj.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie data cannot be empty");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDto.class);
    }


}
