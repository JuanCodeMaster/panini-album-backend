package com.panini.album.catalog;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stickers", indexes = {
        @Index(name = "idx_stickers_code", columnList = "code", unique = true),
        @Index(name = "idx_stickers_country", columnList = "country_id"),
        @Index(name = "idx_stickers_section", columnList = "section_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;  // NULL para INTRO/MUSEUM

    @Column(nullable = false, unique = true, length = 15)
    private String code;  // '00', 'FWC1', 'ARG1', 'ARG20'

    @Column(name = "number_in_country")
    private Integer numberInCountry;  // 1..20 para stickers de equipo, NULL en otros

    @Column(name = "display_name", nullable = false, length = 120)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "sticker_type", nullable = false, length = 20)
    private StickerType stickerType;

    @Column(name = "is_foil", nullable = false)
    private boolean foil;

    @Column(name = "is_in_packs", nullable = false)
    private boolean inPacks;

    @Column(name = "image_url", length = 255)
    private String imageUrl;
}
