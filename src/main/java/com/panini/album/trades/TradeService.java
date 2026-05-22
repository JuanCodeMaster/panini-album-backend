package com.panini.album.trades;

import com.panini.album.album.UserStickerRepository;
import com.panini.album.catalog.Sticker;
import com.panini.album.catalog.dto.StickerDto;
import com.panini.album.common.exception.BadRequestException;
import com.panini.album.common.exception.NotFoundException;
import com.panini.album.social.FriendshipRepository;
import com.panini.album.social.FriendshipStatus;
import com.panini.album.social.dto.FriendDto;
import com.panini.album.user.User;
import com.panini.album.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserStickerRepository userStickerRepository;

    @Transactional(readOnly = true)
    public TradeMatchDto getMatches(User me, String friendUsername) {
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + friendUsername));

        if (friend.getId().equals(me.getId())) {
            throw new BadRequestException("No puedes calcular intercambios contigo mismo");
        }

        var rel = friendshipRepository.findBetween(me, friend)
                .filter(f -> f.getStatus() == FriendshipStatus.ACCEPTED)
                .orElseThrow(() -> new BadRequestException("Solo puedes intercambiar con amigos confirmados"));

        // Lo que YO doy: mis repetidas (quantity > 1) que el amigo NO tiene (quantity 0 o sin registro)
        Set<Long> friendHas = userStickerRepository.findByUserIdWithSticker(friend.getId()).stream()
                .filter(us -> us.getQuantity() > 0)
                .map(us -> us.getSticker().getId())
                .collect(Collectors.toSet());

        List<Sticker> myDuplicates = userStickerRepository.findDuplicatesByUser(me.getId()).stream()
                .map(us -> us.getSticker())
                .toList();

        List<StickerDto> youGive = myDuplicates.stream()
                .filter(s -> !friendHas.contains(s.getId()))
                .map(StickerDto::from)
                .toList();

        // Lo que YO recibo: repetidas del amigo que YO no tengo
        Set<Long> iHave = userStickerRepository.findByUserIdWithSticker(me.getId()).stream()
                .filter(us -> us.getQuantity() > 0)
                .map(us -> us.getSticker().getId())
                .collect(Collectors.toSet());

        List<Sticker> friendDuplicates = userStickerRepository.findDuplicatesByUser(friend.getId()).stream()
                .map(us -> us.getSticker())
                .toList();

        List<StickerDto> youReceive = friendDuplicates.stream()
                .filter(s -> !iHave.contains(s.getId()))
                .map(StickerDto::from)
                .toList();

        return new TradeMatchDto(FriendDto.from(friend), youGive, youReceive);
    }

    @Transactional(readOnly = true)
    public List<TradeSuggestionDto> getSuggestions(User me, int limit) {
        int safe = Math.max(1, Math.min(20, limit));

        Set<Long> iHaveIds = userStickerRepository.findByUserIdWithSticker(me.getId()).stream()
                .filter(us -> us.getQuantity() > 0)
                .map(us -> us.getSticker().getId())
                .collect(Collectors.toSet());

        List<Sticker> myDuplicates = userStickerRepository.findDuplicatesByUser(me.getId()).stream()
                .map(us -> us.getSticker())
                .toList();

        var friendships = friendshipRepository.findAcceptedForUser(me.getId());

        List<TradeSuggestionDto> suggestions = new java.util.ArrayList<>();
        for (var f : friendships) {
            User friend = f.getRequester().getId().equals(me.getId())
                    ? f.getAddressee() : f.getRequester();

            Set<Long> friendHasIds = userStickerRepository.findByUserIdWithSticker(friend.getId()).stream()
                    .filter(us -> us.getQuantity() > 0)
                    .map(us -> us.getSticker().getId())
                    .collect(Collectors.toSet());

            List<Sticker> friendDups = userStickerRepository.findDuplicatesByUser(friend.getId()).stream()
                    .map(us -> us.getSticker())
                    .toList();

            List<StickerDto> giveSample = myDuplicates.stream()
                    .filter(s -> !friendHasIds.contains(s.getId()))
                    .map(StickerDto::from)
                    .toList();

            List<StickerDto> receiveSample = friendDups.stream()
                    .filter(s -> !iHaveIds.contains(s.getId()))
                    .map(StickerDto::from)
                    .toList();

            int giveCount = giveSample.size();
            int receiveCount = receiveSample.size();
            if (giveCount + receiveCount == 0) continue;

            suggestions.add(new TradeSuggestionDto(
                    FriendDto.from(friend),
                    giveCount,
                    receiveCount,
                    giveSample.stream().limit(2).toList(),
                    receiveSample.stream().limit(2).toList()
            ));
        }

        suggestions.sort((a, b) -> Integer.compare(
                b.giveCount() + b.receiveCount(),
                a.giveCount() + a.receiveCount()));

        return suggestions.stream().limit(safe).toList();
    }
}
