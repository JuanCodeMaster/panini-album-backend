package com.panini.album.album.dto;

import java.util.List;
import java.util.Map;

public record AlbumSummaryDto(
        int totalStickers,
        int obtained,
        int missing,
        int duplicates,
        double progressPct,
        Map<String, Integer> stickerQuantities,
        List<CountryProgressDto> countries
) {}
