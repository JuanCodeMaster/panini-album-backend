package com.panini.album.social.dto;

import com.panini.album.user.User;

public record FriendDto(
        Long id,
        String username,
        String displayName,
        String avatarUrl
) {
    public static FriendDto from(User u) {
        return new FriendDto(u.getId(), u.getUsername(), u.getDisplayName(), u.getAvatarUrl());
    }
}
