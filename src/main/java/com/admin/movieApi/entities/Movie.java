package com.admin.movieApi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
public class Movie {
    public Movie(Integer movieId, String title, String director, String studio, Set<String> cast, Integer releaseYear, String poster) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.studio = studio;
        this.cast = cast;
        this.releaseYear = releaseYear;
        this.poster = poster;
    }
    public Movie(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false)
    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Director name is required")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "Studio name is required")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> cast;

    @Column(nullable = false)
    @NotNull(message = "Release year is required")
    private Integer releaseYear;

    @Column(nullable = false)
    @NotBlank(message = "Poster name cannot be empty")
    private String poster;

    public Integer getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getStudio() {
        return studio;
    }

    public Set<String> getCast() {
        return cast;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public String getPoster() {
        return poster;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public void setCast(Set<String> cast) {
        this.cast = cast;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
