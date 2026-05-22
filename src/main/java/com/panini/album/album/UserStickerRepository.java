package com.panini.album.album;

import com.panini.album.catalog.Sticker;
import com.panini.album.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStickerRepository extends JpaRepository<UserSticker, Long> {

    @Query("""
            select us from UserSticker us
            join fetch us.sticker s
            left join fetch s.country
            where us.user.id = :userId
            """)
    List<UserSticker> findByUserIdWithSticker(@Param("userId") Long userId);

    Optional<UserSticker> findByUserAndSticker(User user, Sticker sticker);

    @Query("""
            select count(us) from UserSticker us
            where us.user.id = :userId and us.sticker.country.code = :countryCode
            """)
    long countCountryStickers(@Param("userId") Long userId, @Param("countryCode") String countryCode);

    @Query("""
            select us.sticker.country.code as code, count(us) as obtained
            from UserSticker us
            where us.user.id = :userId and us.sticker.country is not null
            group by us.sticker.country.code
            """)
    List<CountryProgressRow> countByCountry(@Param("userId") Long userId);

    @Query("""
            select us from UserSticker us
            join fetch us.sticker s
            left join fetch s.country
            where us.user.id = :userId and us.quantity > 1
            order by s.country.displayOrder asc nulls first, s.numberInCountry asc
            """)
    List<UserSticker> findDuplicatesByUser(@Param("userId") Long userId);

    @Query("""
            select us from UserSticker us
            join fetch us.sticker s
            left join fetch s.country
            where us.user.id = :userId
            order by us.updatedAt desc, us.obtainedAt desc
            """)
    List<UserSticker> findRecentByUser(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);

    interface CountryProgressRow {
        String getCode();
        Long getObtained();
    }
}
