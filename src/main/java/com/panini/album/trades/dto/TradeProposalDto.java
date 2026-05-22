package com.panini.album.trades.dto;

import com.panini.album.social.dto.FriendDto;
import com.panini.album.trades.TradeProposal;
import com.panini.album.trades.TradeProposalStatus;

import java.time.Instant;
import java.util.List;

public record TradeProposalDto(
        Long id,
        FriendDto requester,
        FriendDto addressee,
        List<String> stickersGiven,
        List<String> stickersReceived,
        boolean gift,
        String message,
        TradeProposalStatus status,
        Instant createdAt,
        Instant respondedAt
) {
    public static TradeProposalDto from(TradeProposal p) {
        return new TradeProposalDto(
                p.getId(),
                FriendDto.from(p.getRequester()),
                FriendDto.from(p.getAddressee()),
                List.copyOf(p.getStickersGiven()),
                List.copyOf(p.getStickersReceived()),
                p.isGift(),
                p.getMessage(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getRespondedAt()
        );
    }
}
