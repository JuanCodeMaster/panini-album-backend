package com.panini.album.trades.dto;

import com.panini.album.trades.TradeMessage;

public record TradeMessageDto(
        Long id,
        Long proposalId,
        String senderUsername,
        String senderDisplayName,
        String body,
        String createdAt
) {
    public static TradeMessageDto from(TradeMessage m) {
        return new TradeMessageDto(
                m.getId(),
                m.getProposal().getId(),
                m.getSender().getUsername(),
                m.getSender().getDisplayName(),
                m.getBody(),
                m.getCreatedAt().toString()
        );
    }
}
