package kg.alfit.tasklist.service;

import kg.alfit.tasklist.web.dto.auth.JwtRequest;
import kg.alfit.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
