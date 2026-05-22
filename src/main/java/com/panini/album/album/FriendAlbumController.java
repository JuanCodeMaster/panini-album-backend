package com.panini.album.album;

import com.panini.album.album.dto.AlbumSummaryDto;
import com.panini.album.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/album/users")
@RequiredArgsConstructor
public class FriendAlbumController {

    private final AlbumService albumService;

    @GetMapping("/{username}/summary")
    public AlbumSummaryDto friendSummary(
            @AuthenticationPrincipal User me,
            @PathVariable String username
    ) {
        return albumService.getFriendSummary(me, username);
    }
}
