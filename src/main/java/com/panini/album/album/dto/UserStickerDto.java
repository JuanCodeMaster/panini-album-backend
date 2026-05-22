package com.panini.album.album.dto;

import com.panini.album.album.UserSticker;

import java.time.Instant;

public record UserStickerDto(
        String stickerCode,
        String countryCode,
        Integer numberInCountry,
        int quantity,
        Instant obtainedAt,
        Instant updatedAt
) {
    public static UserStickerDto from(UserSticker us) {
        return new UserStickerDto(
                us.getSticker().getCode(),
                us.getSticker().getCountry() != null ? us.getSticker().getCountry().getCode() : null,
                us.getSticker().getNumberInCountry(),
                us.getQuantity(),
                us.getObtainedAt(),
                us.getUpdatedAt()
        );
    }
}
