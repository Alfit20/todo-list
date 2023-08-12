package kg.alfit.tasklist.web.controller;

import kg.alfit.tasklist.domain.user.User;
import kg.alfit.tasklist.service.AuthService;
import kg.alfit.tasklist.service.UserService;
import kg.alfit.tasklist.web.dto.auth.JwtRequest;
import kg.alfit.tasklist.web.dto.auth.JwtResponse;
import kg.alfit.tasklist.web.dto.user.UserDTO;
import kg.alfit.tasklist.web.dto.validation.OnCreate;
import kg.alfit.tasklist.web.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    AuthService authService;
    UserService userService;
    UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDTO register(@Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDto(userService.create(user));
    }

    @PostMapping("/refresh")
    public JwtResponse refreshToken(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
