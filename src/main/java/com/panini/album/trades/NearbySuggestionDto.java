package com.panini.album.trades;

import com.panini.album.catalog.dto.StickerDto;
import com.panini.album.social.dto.FriendDto;

import java.util.List;

/** Sugerencia de intercambio con un usuario cercano (≤5km) con match mutuo. */
public record NearbySuggestionDto(
        FriendDto user,
        double distanceKm,
        int giveCount,
        int receiveCount,
        List<StickerDto> sampleGive,
        List<StickerDto> sampleReceive
) {}
