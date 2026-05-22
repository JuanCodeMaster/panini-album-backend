package com.panini.album.trades;

import com.panini.album.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/matches/{friendUsername}")
    public TradeMatchDto matches(
            @AuthenticationPrincipal User me,
            @PathVariable String friendUsername
    ) {
        return tradeService.getMatches(me, friendUsername);
    }

    @GetMapping("/suggestions")
    public List<TradeSuggestionDto> suggestions(
            @AuthenticationPrincipal User me,
            @RequestParam(defaultValue = "3") int limit
    ) {
        return tradeService.getSuggestions(me, limit);
    }
}
