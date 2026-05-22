package com.panini.album.trades;

import com.panini.album.album.UserSticker;
import com.panini.album.album.UserStickerRepository;
import com.panini.album.catalog.Sticker;
import com.panini.album.catalog.StickerRepository;
import com.panini.album.common.exception.BadRequestException;
import com.panini.album.common.exception.NotFoundException;
import com.panini.album.social.FriendshipRepository;
import com.panini.album.social.FriendshipStatus;
import com.panini.album.trades.dto.CreateProposalRequest;
import com.panini.album.trades.dto.TradeProposalDto;
import com.panini.album.user.User;
import com.panini.album.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeProposalService {

    private final TradeProposalRepository proposalRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final StickerRepository stickerRepository;
    private final UserStickerRepository userStickerRepository;

    @Transactional
    public TradeProposalDto create(User me, CreateProposalRequest req) {
        User addressee = userRepository.findByUsername(req.addresseeUsername())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + req.addresseeUsername()));

        if (addressee.getId().equals(me.getId())) {
            throw new BadRequestException("No puedes proponer un intercambio contigo mismo");
        }

        friendshipRepository.findBetween(me, addressee)
                .filter(f -> f.getStatus() == FriendshipStatus.ACCEPTED)
                .orElseThrow(() -> new BadRequestException("Solo puedes intercambiar con amigos confirmados"));

        List<String> given = req.stickersGiven() == null ? List.of() : req.stickersGiven();
        List<String> received = req.stickersReceived() == null ? List.of() : req.stickersReceived();

        if (given.isEmpty() && received.isEmpty()) {
            throw new BadRequestException("Selecciona al menos un cromo");
        }

        // Validar que el requester realmente tenga (con qty > 0) los stickers que ofrece
        for (String code : given) {
            Sticker s = stickerRepository.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Sticker no encontrado: " + code));
            UserSticker us = userStickerRepository.findByUserAndSticker(me, s).orElse(null);
            if (us == null || us.getQuantity() < 1) {
                throw new BadRequestException("No tienes el cromo " + code);
            }
        }
        // Validar que el addressee tenga los stickers solicitados
        for (String code : received) {
            Sticker s = stickerRepository.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Sticker no encontrado: " + code));
            UserSticker us = userStickerRepository.findByUserAndSticker(addressee, s).orElse(null);
            if (us == null || us.getQuantity() < 1) {
                throw new BadRequestException("Tu amigo no tiene el cromo " + code);
            }
        }

        TradeProposal proposal = TradeProposal.builder()
                .requester(me)
                .addressee(addressee)
                .stickersGiven(new ArrayList<>(given))
                .stickersReceived(new ArrayList<>(received))
                .message(req.message())
                .status(TradeProposalStatus.PENDING)
                .build();

        return TradeProposalDto.from(proposalRepository.save(proposal));
    }

    @Transactional(readOnly = true)
    public List<TradeProposalDto> incoming(User me) {
        return proposalRepository.findIncomingPending(me.getId()).stream()
                .map(TradeProposalDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TradeProposalDto> outgoing(User me) {
        return proposalRepository.findOutgoingPending(me.getId()).stream()
                .map(TradeProposalDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TradeProposalDto> history(User me) {
        return proposalRepository.findHistory(me.getId()).stream()
                .map(TradeProposalDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public long pendingCount(User me) {
        return proposalRepository.countByAddresseeIdAndStatus(me.getId(), TradeProposalStatus.PENDING);
    }

    @Transactional
    public TradeProposalDto accept(User me, Long proposalId) {
        TradeProposal p = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new NotFoundException("Propuesta no encontrada"));

        if (!p.getAddressee().getId().equals(me.getId())) {
            throw new BadRequestException("Solo el destinatario puede aceptar la propuesta");
        }
        if (p.getStatus() != TradeProposalStatus.PENDING) {
            throw new BadRequestException("La propuesta no está pendiente");
        }

        // Transferencias: stickersGiven van de requester -> addressee
        for (String code : p.getStickersGiven()) {
            transfer(p.getRequester(), p.getAddressee(), code);
        }
        // stickersReceived van de addressee -> requester
        for (String code : p.getStickersReceived()) {
            transfer(p.getAddressee(), p.getRequester(), code);
        }

        p.setStatus(TradeProposalStatus.ACCEPTED);
        p.setRespondedAt(Instant.now());
        return TradeProposalDto.from(proposalRepository.save(p));
    }

    @Transactional
    public void reject(User me, Long proposalId) {
        TradeProposal p = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new NotFoundException("Propuesta no encontrada"));

        if (!p.getAddressee().getId().equals(me.getId())) {
            throw new BadRequestException("Solo el destinatario puede rechazar");
        }
        if (p.getStatus() != TradeProposalStatus.PENDING) {
            throw new BadRequestException("La propuesta no está pendiente");
        }
        p.setStatus(TradeProposalStatus.REJECTED);
        p.setRespondedAt(Instant.now());
        proposalRepository.save(p);
    }

    @Transactional
    public void cancel(User me, Long proposalId) {
        TradeProposal p = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new NotFoundException("Propuesta no encontrada"));

        if (!p.getRequester().getId().equals(me.getId())) {
            throw new BadRequestException("Solo el remitente puede cancelar");
        }
        if (p.getStatus() != TradeProposalStatus.PENDING) {
            throw new BadRequestException("La propuesta no está pendiente");
        }
        p.setStatus(TradeProposalStatus.CANCELLED);
        p.setRespondedAt(Instant.now());
        proposalRepository.save(p);
    }

    private void transfer(User from, User to, String stickerCode) {
        Sticker s = stickerRepository.findByCode(stickerCode)
                .orElseThrow(() -> new NotFoundException("Sticker no encontrado: " + stickerCode));

        UserSticker fromUs = userStickerRepository.findByUserAndSticker(from, s)
                .orElseThrow(() -> new BadRequestException(
                        from.getUsername() + " ya no tiene el cromo " + stickerCode));
        if (fromUs.getQuantity() < 1) {
            throw new BadRequestException(from.getUsername() + " ya no tiene el cromo " + stickerCode);
        }

        // Decrementar del que da
        if (fromUs.getQuantity() == 1) {
            userStickerRepository.delete(fromUs);
        } else {
            fromUs.setQuantity(fromUs.getQuantity() - 1);
            userStickerRepository.save(fromUs);
        }

        // Incrementar al que recibe
        UserSticker toUs = userStickerRepository.findByUserAndSticker(to, s).orElse(null);
        if (toUs == null) {
            toUs = UserSticker.builder()
                    .user(to)
                    .sticker(s)
                    .quantity(1)
                    .build();
        } else {
            toUs.setQuantity(toUs.getQuantity() + 1);
        }
        userStickerRepository.save(toUs);
    }
}
