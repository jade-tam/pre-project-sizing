package vn.gtel.pm2.sizing.service;


import vn.gtel.pm2.sizing.dto.request.AuthLoginRequest;
import vn.gtel.pm2.sizing.dto.request.AuthRefreshRequest;
import vn.gtel.pm2.sizing.dto.request.AuthRegisterRequest;
import vn.gtel.pm2.sizing.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthRegisterRequest request);

    AuthResponse login(AuthLoginRequest request);

    AuthResponse refresh(AuthRefreshRequest request);

    void logout();
}
