package com.panini.album.trades;

import com.panini.album.catalog.dto.StickerDto;
import com.panini.album.social.dto.FriendDto;

import java.util.List;

public record TradeMatchDto(
        FriendDto friend,
        List<StickerDto> youGive,    // Mis repetidas que el amigo no tiene
        List<StickerDto> youReceive  // Repetidas del amigo que yo no tengo
) {}
