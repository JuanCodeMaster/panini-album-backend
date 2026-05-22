package com.panini.album.album;

import com.panini.album.album.dto.*;
import com.panini.album.catalog.Country;
import com.panini.album.catalog.CountryRepository;
import com.panini.album.catalog.Sticker;
import com.panini.album.catalog.StickerRepository;
import com.panini.album.catalog.dto.StickerDto;
import com.panini.album.common.exception.BadRequestException;
import com.panini.album.common.exception.NotFoundException;
import com.panini.album.social.FriendshipRepository;
import com.panini.album.social.FriendshipStatus;
import com.panini.album.user.User;
import com.panini.album.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private static final int STICKERS_PER_COUNTRY = 20;

    private final UserStickerRepository userStickerRepository;
    private final StickerRepository stickerRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional(readOnly = true)
    public List<UserStickerDto> getUserStickers(User user) {
        return userStickerRepository.findByUserIdWithSticker(user.getId()).stream()
                .map(UserStickerDto::from)
                .toList();
    }

    @Transactional
    public UserStickerDto setQuantity(User user, String stickerCode, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        Sticker sticker = stickerRepository.findByCode(stickerCode)
                .orElseThrow(() -> new NotFoundException("Sticker no encontrado: " + stickerCode));

        UserSticker us = userStickerRepository.findByUserAndSticker(user, sticker).orElse(null);

        if (quantity == 0) {
            if (us != null) userStickerRepository.delete(us);
            return new UserStickerDto(stickerCode, sticker.getCountry() != null ? sticker.getCountry().getCode() : null,
                    sticker.getNumberInCountry(), 0, null, null);
        }

        if (us == null) {
            us = UserSticker.builder()
                    .user(user)
                    .sticker(sticker)
                    .quantity(quantity)
                    .build();
        } else {
            us.setQuantity(quantity);
        }
        userStickerRepository.save(us);
        return UserStickerDto.from(us);
    }

    @Transactional
    public UserStickerDto increment(User user, String stickerCode) {
        Sticker sticker = stickerRepository.findByCode(stickerCode)
                .orElseThrow(() -> new NotFoundException("Sticker no encontrado: " + stickerCode));

        UserSticker us = userStickerRepository.findByUserAndSticker(user, sticker)
                .orElseGet(() -> UserSticker.builder()
                        .user(user)
                        .sticker(sticker)
                        .quantity(0)
                        .build());
        us.setQuantity(us.getQuantity() + 1);
        userStickerRepository.save(us);
        return UserStickerDto.from(us);
    }

    @Transactional
    public UserStickerDto decrement(User user, String stickerCode) {
        Sticker sticker = stickerRepository.findByCode(stickerCode)
                .orElseThrow(() -> new NotFoundException("Sticker no encontrado: " + stickerCode));

        UserSticker us = userStickerRepository.findByUserAndSticker(user, sticker).orElse(null);
        if (us == null || us.getQuantity() <= 1) {
            if (us != null) userStickerRepository.delete(us);
            return new UserStickerDto(stickerCode,
                    sticker.getCountry() != null ? sticker.getCountry().getCode() : null,
                    sticker.getNumberInCountry(), 0, null, null);
        }
        us.setQuantity(us.getQuantity() - 1);
        userStickerRepository.save(us);
        return UserStickerDto.from(us);
    }

    @Transactional(readOnly = true)
    public List<StickerDto> getMissing(User user) {
        return stickerRepository.findMissingForUser(user.getId()).stream()
                .map(StickerDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserStickerDto> getDuplicates(User user) {
        return userStickerRepository.findDuplicatesByUser(user.getId()).stream()
                .map(UserStickerDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserStickerDto> getRecent(User user, int limit) {
        int safe = Math.max(1, Math.min(20, limit));
        return userStickerRepository
                .findRecentByUser(user.getId(), org.springframework.data.domain.PageRequest.of(0, safe))
                .stream()
                .map(UserStickerDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlbumSummaryDto getFriendSummary(User me, String friendUsername) {
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + friendUsername));

        if (!friend.getId().equals(me.getId())) {
            friendshipRepository.findBetween(me, friend)
                    .filter(f -> f.getStatus() == FriendshipStatus.ACCEPTED)
                    .orElseThrow(() -> new BadRequestException("Solo puedes ver el álbum de tus amigos"));
        }

        return getSummary(friend);
    }

    @Transactional(readOnly = true)
    public AlbumSummaryDto getSummary(User user) {
        List<UserSticker> mine = userStickerRepository.findByUserIdWithSticker(user.getId());
        long totalCatalog = stickerRepository.count();

        Map<String, Integer> quantities = new HashMap<>();
        int obtained = 0;
        int duplicates = 0;
        for (UserSticker us : mine) {
            quantities.put(us.getSticker().getCode(), us.getQuantity());
            if (us.getQuantity() > 0) obtained++;
            if (us.getQuantity() > 1) duplicates += us.getQuantity() - 1;
        }
        int missing = (int) totalCatalog - obtained;
        double pct = totalCatalog == 0 ? 0.0 : Math.round((obtained * 10000.0) / totalCatalog) / 100.0;

        List<Country> countries = countryRepository.findAllByOrderByDisplayOrderAsc();
        Map<String, Long> obtainedByCountry = new HashMap<>();
        userStickerRepository.countByCountry(user.getId())
                .forEach(r -> obtainedByCountry.put(r.getCode(), r.getObtained()));

        List<CountryProgressDto> countryProgress = countries.stream()
                .map(c -> {
                    long obt = obtainedByCountry.getOrDefault(c.getCode(), 0L);
                    double cpct = Math.round((obt * 10000.0) / STICKERS_PER_COUNTRY) / 100.0;
                    return new CountryProgressDto(c.getCode(), STICKERS_PER_COUNTRY, (int) obt, cpct);
                })
                .toList();

        return new AlbumSummaryDto((int) totalCatalog, obtained, missing, duplicates, pct, quantities, countryProgress);
    }
}
