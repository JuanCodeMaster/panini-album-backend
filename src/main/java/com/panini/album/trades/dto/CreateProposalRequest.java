package com.panini.album.trades.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateProposalRequest(
        @NotBlank String addresseeUsername,
        List<String> stickersGiven,
        List<String> stickersReceived,
        @Size(max = 280) String message
) {}
