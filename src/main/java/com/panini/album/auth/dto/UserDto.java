package com.panini.album.auth.dto;

import com.panini.album.user.User;

public record UserDto(
        Long id,
        String username,
        String email,
        String displayName,
        String avatarUrl
) {
    public static UserDto from(User u) {
        return new UserDto(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getDisplayName(),
                u.getAvatarUrl()
        );
    }
}
