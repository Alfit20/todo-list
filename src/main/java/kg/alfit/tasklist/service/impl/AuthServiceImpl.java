package kg.alfit.tasklist.service.impl;

import kg.alfit.tasklist.service.AuthService;
import kg.alfit.tasklist.web.dto.auth.JwtRequest;
import kg.alfit.tasklist.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
