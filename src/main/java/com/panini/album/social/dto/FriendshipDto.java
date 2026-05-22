package com.panini.album.social.dto;

import com.panini.album.social.Friendship;
import com.panini.album.social.FriendshipStatus;

import java.time.Instant;

public record FriendshipDto(
        Long id,
        FriendDto requester,
        FriendDto addressee,
        FriendshipStatus status,
        Instant createdAt,
        Instant acceptedAt
) {
    public static FriendshipDto from(Friendship f) {
        return new FriendshipDto(
                f.getId(),
                FriendDto.from(f.getRequester()),
                FriendDto.from(f.getAddressee()),
                f.getStatus(),
                f.getCreatedAt(),
                f.getAcceptedAt()
        );
    }
}
