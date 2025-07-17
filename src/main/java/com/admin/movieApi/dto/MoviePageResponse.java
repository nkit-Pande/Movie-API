package com.admin.movieApi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record MoviePageResponse(List<MovieDto> movies, Integer pageNumber, Integer pageSize, int totalElements,
                                int totalPages, boolean last) {

}
