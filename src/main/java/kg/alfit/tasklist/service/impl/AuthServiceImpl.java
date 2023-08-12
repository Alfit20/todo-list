package kg.alfit.tasklist.service.impl;

import kg.alfit.tasklist.domain.user.User;
import kg.alfit.tasklist.service.AuthService;
import kg.alfit.tasklist.service.UserService;
import kg.alfit.tasklist.web.dto.auth.JwtRequest;
import kg.alfit.tasklist.web.dto.auth.JwtResponse;
import kg.alfit.tasklist.web.security.JwtTokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    final AuthenticationManager authenticationManager;
    final UserService userService;
    final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userService.getByUsername(loginRequest.getUsername());
        return JwtResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(jwtTokenProvider.createAccessToken(user.getId(),
                        user.getUsername(), user.getRoles()))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()))
                .build();
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
