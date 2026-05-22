package com.panini.album.trades;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeProposalRepository extends JpaRepository<TradeProposal, Long> {

    @Query("""
            select p from TradeProposal p
            join fetch p.requester
            join fetch p.addressee
            where p.addressee.id = :userId and p.status = com.panini.album.trades.TradeProposalStatus.PENDING
            order by p.createdAt desc
            """)
    List<TradeProposal> findIncomingPending(@Param("userId") Long userId);

    @Query("""
            select p from TradeProposal p
            join fetch p.requester
            join fetch p.addressee
            where p.requester.id = :userId and p.status = com.panini.album.trades.TradeProposalStatus.PENDING
            order by p.createdAt desc
            """)
    List<TradeProposal> findOutgoingPending(@Param("userId") Long userId);

    @Query("""
            select p from TradeProposal p
            join fetch p.requester
            join fetch p.addressee
            where (p.requester.id = :userId or p.addressee.id = :userId)
              and p.status <> com.panini.album.trades.TradeProposalStatus.PENDING
            order by coalesce(p.respondedAt, p.createdAt) desc
            """)
    List<TradeProposal> findHistory(@Param("userId") Long userId);

    long countByAddresseeIdAndStatus(Long addresseeId, TradeProposalStatus status);
}
