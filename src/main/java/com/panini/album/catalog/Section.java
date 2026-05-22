package com.panini.album.catalog;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;  // INTRO, MUSEUM, TEAM

    @Column(nullable = false, length = 60)
    private String name;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
}
