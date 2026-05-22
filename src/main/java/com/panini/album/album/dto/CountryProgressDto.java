package com.panini.album.album.dto;

public record CountryProgressDto(
        String countryCode,
        int total,
        int obtained,
        double progressPct
) {}
