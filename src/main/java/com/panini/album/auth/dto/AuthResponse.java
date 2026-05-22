package com.panini.album.auth.dto;

public record AuthResponse(
        String token,
        UserDto user
) {}
