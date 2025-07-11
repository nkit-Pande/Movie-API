package com.admin.movieApi.auth.utils;

public record ChangePassword (
        String password,
        String newPassword
){
}
