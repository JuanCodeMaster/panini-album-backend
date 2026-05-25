package com.panini.album.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

/**
 * Ubicación opt-in del usuario, usada para sugerencias de intercambio por cercanía.
 * El cliente envía coordenadas solo si el usuario activó "compartir ubicación".
 */
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public Map<String, Object> status(@AuthenticationPrincipal User me) {
        return Map.of(
                "sharing", me.isLocationSharing(),
                "updatedAt", me.getLocationUpdatedAt() == null ? "" : me.getLocationUpdatedAt().toString()
        );
    }

    @PutMapping
    @Transactional
    public Map<String, Object> update(
            @AuthenticationPrincipal User me,
            @RequestBody LocationRequest req
    ) {
        me.setLatitude(req.latitude());
        me.setLongitude(req.longitude());
        me.setLocationSharing(true);
        me.setLocationUpdatedAt(Instant.now());
        userRepository.save(me);
        return Map.of("sharing", true);
    }

    @DeleteMapping
    @Transactional
    public Map<String, Object> disable(@AuthenticationPrincipal User me) {
        me.setLocationSharing(false);
        me.setLatitude(null);
        me.setLongitude(null);
        userRepository.save(me);
        return Map.of("sharing", false);
    }

    public record LocationRequest(Double latitude, Double longitude) {}
}
