package com.panini.album.auth;

import com.panini.album.auth.dto.AuthResponse;
import com.panini.album.auth.dto.LoginRequest;
import com.panini.album.auth.dto.RegisterRequest;
import com.panini.album.auth.dto.UserDto;
import com.panini.album.common.exception.BadRequestException;
import com.panini.album.common.exception.UnauthorizedException;
import com.panini.album.user.User;
import com.panini.album.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new BadRequestException("El nombre de usuario ya está en uso");
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new BadRequestException("El email ya está registrado");
        }

        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .displayName(req.displayName() != null ? req.displayName() : req.username())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getUsername());
        return new AuthResponse(token, UserDto.from(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.usernameOrEmail())
                .or(() -> userRepository.findByEmail(req.usernameOrEmail()))
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user.getId(), user.getUsername());
        return new AuthResponse(token, UserDto.from(user));
    }
}
