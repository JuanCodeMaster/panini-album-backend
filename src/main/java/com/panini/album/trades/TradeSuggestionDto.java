package com.panini.album.trades;

import com.panini.album.catalog.dto.StickerDto;
import com.panini.album.social.dto.FriendDto;

import java.util.List;

public record TradeSuggestionDto(
        FriendDto friend,
        int giveCount,
        int receiveCount,
        List<StickerDto> sampleGive,
        List<StickerDto> sampleReceive
) {}
