package com.panini.album.album.dto;

import jakarta.validation.constraints.Min;

public record SetQuantityRequest(
        @Min(0) int quantity
) {}
