package com.panini.album.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StickerRepository extends org.springframework.data.jpa.repository.JpaRepository<Sticker, Long> {

    Optional<Sticker> findByCode(String code);


    @Query("""
            select s from Sticker s
            join fetch s.section
            left join fetch s.country
            where s.country.code = :code
            order by s.numberInCountry asc
            """)
    List<Sticker> findByCountryCode(String code);

    @Query("""
            select s from Sticker s
            join fetch s.section
            left join fetch s.country
            order by s.country.displayOrder asc nulls first, s.numberInCountry asc, s.code asc
            """)
    List<Sticker> findAllWithRelations();

    @Query("""
            select s from Sticker s
            join fetch s.section
            where s.section.code = :sectionCode and s.country is null
            order by s.code asc
            """)
    List<Sticker> findBySectionCodeAndNoCountry(String sectionCode);

    long countBySection(Section section);

    @Query("""
            select s from Sticker s
            join fetch s.section
            left join fetch s.country
            where s.id not in (
                select us.sticker.id from UserSticker us where us.user.id = :userId
            )
            order by s.country.displayOrder asc nulls first, s.numberInCountry asc, s.code asc
            """)
    List<Sticker> findMissingForUser(Long userId);

    /** Stickers del TEAM con país pero sin pos válido (1..20) — basura de seeds previos. */
    @Query("""
            select s from Sticker s
            where s.section.code = 'TEAM'
              and s.country.code = :countryCode
              and (s.numberInCountry is null or s.numberInCountry < 1 or s.numberInCountry > 20)
            """)
    List<Sticker> findOrphanCountryStickers(String countryCode);
}
