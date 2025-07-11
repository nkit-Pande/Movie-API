package com.admin.movieApi.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;


@Data
public class MovieDto {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    public MovieDto(Integer movieId, String title, String director, String studio, Set<String> cast, Integer releaseYear, String poster, String postUrl) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.studio = studio;
        this.cast = cast;
        this.releaseYear = releaseYear;
        this.poster = poster;
        this.postUrl = postUrl;
    }

    public MovieDto() {

    }

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Director name is required")
    private String director;


    @NotBlank(message = "Studio name is required")
    private String studio;

    private Set<String> cast;


    @NotNull(message = "Release year is required")
    private Integer releaseYear;


    @NotBlank(message = "Poster name cannot be empty")
    private String poster;

    @NotBlank(message = "Poster URL or path cannot be empty")
    private String postUrl;


    public Integer getMovieId() {
        return movieId;
    }


    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public Set<String> getCast() {
        return cast;
    }

    public void setCast(Set<String> cast) {
        this.cast = cast;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

}
