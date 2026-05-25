package com.panini.album.trades;

import com.panini.album.trades.dto.CreateProposalRequest;
import com.panini.album.trades.dto.TradeMessageDto;
import com.panini.album.trades.dto.TradeProposalDto;
import com.panini.album.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trades/proposals")
@RequiredArgsConstructor
public class TradeProposalController {

    private final TradeProposalService proposalService;

    @PostMapping
    public TradeProposalDto create(
            @AuthenticationPrincipal User me,
            @Valid @RequestBody CreateProposalRequest body
    ) {
        return proposalService.create(me, body);
    }

    @GetMapping("/incoming")
    public List<TradeProposalDto> incoming(@AuthenticationPrincipal User me) {
        return proposalService.incoming(me);
    }

    @GetMapping("/outgoing")
    public List<TradeProposalDto> outgoing(@AuthenticationPrincipal User me) {
        return proposalService.outgoing(me);
    }

    @GetMapping("/history")
    public List<TradeProposalDto> history(@AuthenticationPrincipal User me) {
        return proposalService.history(me);
    }

    @GetMapping("/pending-count")
    public Map<String, Long> pendingCount(@AuthenticationPrincipal User me) {
        return Map.of("count", proposalService.pendingCount(me));
    }

    @PostMapping("/{id}/accept")
    public TradeProposalDto accept(
            @AuthenticationPrincipal User me,
            @PathVariable Long id
    ) {
        return proposalService.accept(me, id);
    }

    @PostMapping("/{id}/reject")
    public Map<String, Boolean> reject(
            @AuthenticationPrincipal User me,
            @PathVariable Long id
    ) {
        proposalService.reject(me, id);
        return Map.of("ok", true);
    }

    @PostMapping("/{id}/cancel")
    public Map<String, Boolean> cancel(
            @AuthenticationPrincipal User me,
            @PathVariable Long id
    ) {
        proposalService.cancel(me, id);
        return Map.of("ok", true);
    }

    // ── Chat ──

    @GetMapping("/{id}/messages")
    public List<TradeMessageDto> messages(
            @AuthenticationPrincipal User me,
            @PathVariable Long id
    ) {
        return proposalService.listMessages(me, id);
    }

    @PostMapping("/{id}/messages")
    public TradeMessageDto send(
            @AuthenticationPrincipal User me,
            @PathVariable Long id,
            @RequestBody MessageRequest body
    ) {
        return proposalService.sendMessage(me, id, body.body());
    }

    public record MessageRequest(String body) {}
}
