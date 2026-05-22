package com.panini.album.catalog.dto;

import com.panini.album.catalog.Sticker;
import com.panini.album.catalog.StickerType;

public record StickerDto(
        Long id,
        String code,
        Integer numberInCountry,
        String displayName,
        StickerType stickerType,
        boolean foil,
        boolean inPacks,
        String countryCode,
        String sectionCode,
        String imageUrl
) {
    public static StickerDto from(Sticker s) {
        return new StickerDto(
                s.getId(),
                s.getCode(),
                s.getNumberInCountry(),
                s.getDisplayName(),
                s.getStickerType(),
                s.isFoil(),
                s.isInPacks(),
                s.getCountry() != null ? s.getCountry().getCode() : null,
                s.getSection().getCode(),
                s.getImageUrl()
        );
    }
}
