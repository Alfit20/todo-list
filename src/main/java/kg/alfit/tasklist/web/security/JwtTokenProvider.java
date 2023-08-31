package kg.alfit.tasklist.web.security;


import kg.alfit.tasklist.domain.exception.AccessDeniedException;
import kg.alfit.tasklist.domain.user.Role;
import kg.alfit.tasklist.domain.user.User;
import kg.alfit.tasklist.service.UserService;
import kg.alfit.tasklist.service.props.JwtProperties;
import kg.alfit.tasklist.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class JwtTokenProvider {

    final JwtProperties jwtProperties;
    final UserDetailsService userDetailsService;
    final UserService userService;
    Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));
        Instant expirationDate = Instant.now().plus(jwtProperties.getAccess(), ChronoUnit.HOURS);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expirationDate))
                .signWith(key)
                .compact();

    }

    public String createRefreshToken(Long userId, String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        Instant expirationDate = Instant.now().plus(jwtProperties.getAccess(), ChronoUnit.HOURS);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expirationDate))
                .signWith(key)
                .compact();

    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        return JwtResponse.builder()
                .id(userId)
                .username(user.getUsername())
                .accessToken(createAccessToken(userId, user.getUsername(), user.getRoles()))
                .refreshToken(createRefreshToken(userId, user.getUsername()))
                .build();
    }

    public boolean validateToken(final String token) {
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private String getId(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    private String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}
