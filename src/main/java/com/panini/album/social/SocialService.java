package com.panini.album.social;

import com.panini.album.common.exception.BadRequestException;
import com.panini.album.common.exception.NotFoundException;
import com.panini.album.social.dto.FriendDto;
import com.panini.album.social.dto.FriendshipDto;
import com.panini.album.user.User;
import com.panini.album.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional(readOnly = true)
    public List<FriendDto> searchUsers(User me, String query) {
        if (query == null || query.isBlank() || query.length() < 2) {
            return List.of();
        }
        String q = query.trim().toLowerCase();
        return userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(me.getId()))
                .filter(u -> u.getUsername().toLowerCase().contains(q)
                        || (u.getDisplayName() != null && u.getDisplayName().toLowerCase().contains(q))
                        || u.getEmail().toLowerCase().contains(q))
                .limit(20)
                .map(FriendDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendDto> listFriends(User me) {
        return friendshipRepository.findAcceptedForUser(me.getId()).stream()
                .map(f -> f.getRequester().getId().equals(me.getId()) ? f.getAddressee() : f.getRequester())
                .map(FriendDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendshipDto> incomingRequests(User me) {
        return friendshipRepository.findIncomingPending(me.getId()).stream()
                .map(FriendshipDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendshipDto> outgoingRequests(User me) {
        return friendshipRepository.findOutgoingPending(me.getId()).stream()
                .map(FriendshipDto::from)
                .toList();
    }

    @Transactional
    public FriendshipDto sendRequest(User me, String addresseeUsername) {
        User addressee = userRepository.findByUsername(addresseeUsername)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + addresseeUsername));

        if (addressee.getId().equals(me.getId())) {
            throw new BadRequestException("No puedes agregarte a ti mismo");
        }

        Optional<Friendship> existing = friendshipRepository.findBetween(me, addressee);
        if (existing.isPresent()) {
            Friendship f = existing.get();
            if (f.getStatus() == FriendshipStatus.ACCEPTED) {
                throw new BadRequestException("Ya son amigos");
            }
            if (f.getStatus() == FriendshipStatus.PENDING) {
                if (f.getAddressee().getId().equals(me.getId())) {
                    // El otro ya me había enviado solicitud → aceptar
                    return accept(me, f.getId());
                }
                throw new BadRequestException("Ya enviaste una solicitud a este usuario");
            }
            // REJECTED → permitir reintentar reusando el registro
            f.setStatus(FriendshipStatus.PENDING);
            f.setRequester(me);
            f.setAddressee(addressee);
            f.setAcceptedAt(null);
            return FriendshipDto.from(friendshipRepository.save(f));
        }

        Friendship f = Friendship.builder()
                .requester(me)
                .addressee(addressee)
                .status(FriendshipStatus.PENDING)
                .build();
        return FriendshipDto.from(friendshipRepository.save(f));
    }

    @Transactional
    public FriendshipDto accept(User me, Long friendshipId) {
        Friendship f = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        if (!f.getAddressee().getId().equals(me.getId())) {
            throw new BadRequestException("Solo el destinatario puede aceptar la solicitud");
        }
        if (f.getStatus() != FriendshipStatus.PENDING) {
            throw new BadRequestException("La solicitud no está pendiente");
        }
        f.setStatus(FriendshipStatus.ACCEPTED);
        f.setAcceptedAt(Instant.now());
        return FriendshipDto.from(friendshipRepository.save(f));
    }

    @Transactional
    public void reject(User me, Long friendshipId) {
        Friendship f = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        if (!f.getAddressee().getId().equals(me.getId()) && !f.getRequester().getId().equals(me.getId())) {
            throw new BadRequestException("No tienes permiso sobre esta solicitud");
        }
        friendshipRepository.delete(f);
    }

    @Transactional
    public void unfriend(User me, String username) {
        User other = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + username));
        Friendship f = friendshipRepository.findBetween(me, other)
                .orElseThrow(() -> new NotFoundException("No son amigos"));
        friendshipRepository.delete(f);
    }
}
