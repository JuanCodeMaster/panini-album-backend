package com.panini.album.trades;

import com.panini.album.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trade_proposals", indexes = {
        @Index(name = "idx_trade_addressee_status", columnList = "addressee_id, status"),
        @Index(name = "idx_trade_requester_status", columnList = "requester_id, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "addressee_id", nullable = false)
    private User addressee;

    /** Stickers que el requester ofrece dar (codes). */
    @ElementCollection
    @CollectionTable(name = "trade_proposal_given",
            joinColumns = @JoinColumn(name = "proposal_id"))
    @Column(name = "sticker_code", length = 15)
    @Builder.Default
    private List<String> stickersGiven = new ArrayList<>();

    /** Stickers que el requester quiere recibir (vacío = regalo). */
    @ElementCollection
    @CollectionTable(name = "trade_proposal_received",
            joinColumns = @JoinColumn(name = "proposal_id"))
    @Column(name = "sticker_code", length = 15)
    @Builder.Default
    private List<String> stickersReceived = new ArrayList<>();

    @Column(length = 280)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TradeProposalStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (status == null) status = TradeProposalStatus.PENDING;
    }

    public boolean isGift() {
        return stickersReceived == null || stickersReceived.isEmpty();
    }
}
