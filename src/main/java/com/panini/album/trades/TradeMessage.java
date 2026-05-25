package com.panini.album.trades;

import com.panini.album.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/** Mensaje de chat dentro de una propuesta de intercambio. */
@Entity
@Table(name = "trade_messages", indexes = {
        @Index(name = "idx_trade_msg_proposal", columnList = "proposal_id, created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proposal_id", nullable = false)
    private TradeProposal proposal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false, length = 500)
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
