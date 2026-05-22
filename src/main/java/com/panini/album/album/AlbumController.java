package com.panini.album.album;

import com.panini.album.album.dto.AlbumSummaryDto;
import com.panini.album.album.dto.SetQuantityRequest;
import com.panini.album.album.dto.UserStickerDto;
import com.panini.album.catalog.dto.StickerDto;
import com.panini.album.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/album/me")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/stickers")
    public List<UserStickerDto> myStickers(@AuthenticationPrincipal User user) {
        return albumService.getUserStickers(user);
    }

    @GetMapping("/summary")
    public AlbumSummaryDto summary(@AuthenticationPrincipal User user) {
        return albumService.getSummary(user);
    }

    @GetMapping("/missing")
    public List<StickerDto> missing(@AuthenticationPrincipal User user) {
        return albumService.getMissing(user);
    }

    @GetMapping("/duplicates")
    public List<UserStickerDto> duplicates(@AuthenticationPrincipal User user) {
        return albumService.getDuplicates(user);
    }

    @GetMapping("/recent")
    public List<UserStickerDto> recent(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return albumService.getRecent(user, limit);
    }

    @PutMapping("/stickers/{code}")
    public UserStickerDto setQuantity(
            @AuthenticationPrincipal User user,
            @PathVariable String code,
            @Valid @RequestBody SetQuantityRequest body
    ) {
        return albumService.setQuantity(user, code, body.quantity());
    }

    @PostMapping("/stickers/{code}/increment")
    public UserStickerDto increment(
            @AuthenticationPrincipal User user,
            @PathVariable String code
    ) {
        return albumService.increment(user, code);
    }

    @PostMapping("/stickers/{code}/decrement")
    public UserStickerDto decrement(
            @AuthenticationPrincipal User user,
            @PathVariable String code
    ) {
        return albumService.decrement(user, code);
    }
}
