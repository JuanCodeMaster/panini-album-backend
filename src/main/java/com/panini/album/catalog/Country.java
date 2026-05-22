package com.panini.album.catalog;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries", indexes = {
        @Index(name = "idx_countries_code", columnList = "code", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 3)
    private String code;  // ARG, BRA, USA, ...

    @Column(nullable = false, length = 80)
    private String name;  // Argentina, Brasil, Estados Unidos, ...

    @Column(name = "iso2", length = 6)
    private String iso2;  // AR, BR, US (para flagcdn.com); admite GB-ENG/GB-SCT

    @Column(name = "wc_group", length = 2)
    private String wcGroup;  // Grupo del Mundial 2026: A..L

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
}
