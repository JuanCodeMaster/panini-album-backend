package com.panini.album.social;

import com.panini.album.social.dto.FriendDto;
import com.panini.album.social.dto.FriendshipDto;
import com.panini.album.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;

    @GetMapping("/users/search")
    public List<FriendDto> search(
            @AuthenticationPrincipal User me,
            @RequestParam("q") String query
    ) {
        return socialService.searchUsers(me, query);
    }

    @GetMapping("/friends")
    public List<FriendDto> friends(@AuthenticationPrincipal User me) {
        return socialService.listFriends(me);
    }

    @GetMapping("/requests/incoming")
    public List<FriendshipDto> incoming(@AuthenticationPrincipal User me) {
        return socialService.incomingRequests(me);
    }

    @GetMapping("/requests/outgoing")
    public List<FriendshipDto> outgoing(@AuthenticationPrincipal User me) {
        return socialService.outgoingRequests(me);
    }

    @PostMapping("/requests/{username}")
    public FriendshipDto send(
            @AuthenticationPrincipal User me,
            @PathVariable String username
    ) {
        return socialService.sendRequest(me, username);
    }

    @PostMapping("/requests/{id}/accept")
    public FriendshipDto accept(
            @AuthenticationPrincipal User me,
            @PathVariable Long id
    ) {
        return socialService.accept(me, id);
    }

    @DeleteMapping("/requests/{id}")
    public Map<String, Boolean> reject(
            @AuthenticationPrincipal User me,
            @PathVariable Long id
    ) {
        socialService.reject(me, id);
        return Map.of("ok", true);
    }

    @DeleteMapping("/friends/{username}")
    public Map<String, Boolean> unfriend(
            @AuthenticationPrincipal User me,
            @PathVariable String username
    ) {
        socialService.unfriend(me, username);
        return Map.of("ok", true);
    }
}
