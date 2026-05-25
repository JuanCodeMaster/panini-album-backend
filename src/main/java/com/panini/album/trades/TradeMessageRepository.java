package com.panini.album.trades;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradeMessageRepository extends JpaRepository<TradeMessage, Long> {

    @Query("""
            select m from TradeMessage m
            join fetch m.sender
            where m.proposal.id = :proposalId
            order by m.createdAt asc
            """)
    List<TradeMessage> findByProposalId(Long proposalId);
}
