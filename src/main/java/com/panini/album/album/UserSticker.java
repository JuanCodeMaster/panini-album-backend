package com.panini.album.album;

import com.panini.album.catalog.Sticker;
import com.panini.album.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_stickers",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_sticker", columnNames = {"user_id", "sticker_id"}),
        indexes = {
                @Index(name = "idx_user_stickers_user", columnList = "user_id"),
                @Index(name = "idx_user_stickers_sticker", columnList = "sticker_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sticker_id", nullable = false)
    private Sticker sticker;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "obtained_at", nullable = false, updatable = false)
    private Instant obtainedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (obtainedAt == null) obtainedAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
