package vn.gtel.pm2.sizing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.gtel.pm2.sizing.dto.common.ApiResponse;
import vn.gtel.pm2.sizing.dto.request.AuthLoginRequest;
import vn.gtel.pm2.sizing.dto.request.AuthRefreshRequest;
import vn.gtel.pm2.sizing.dto.request.AuthRegisterRequest;
import vn.gtel.pm2.sizing.dto.response.AuthResponse;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.i18n.MessageService;
import vn.gtel.pm2.sizing.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MessageService messageService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        ResponseCode code = ResponseCode.ACCOUNT_CREATED;
        return ApiResponse.success(HttpStatus.CREATED, code.name(), messageService.get(code.getMessageKey()), authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        ResponseCode code = ResponseCode.SUCCESS;
        return ApiResponse.success(HttpStatus.OK, code.name(), messageService.get(code.getMessageKey()), authService.login(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody AuthRefreshRequest request) {
        ResponseCode code = ResponseCode.SUCCESS;
        return ApiResponse.success(HttpStatus.OK, code.name(), messageService.get(code.getMessageKey()), authService.refresh(request));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse logout() {
        authService.logout();
        ResponseCode code = ResponseCode.SUCCESS;
        return ApiResponse.success(HttpStatus.OK, code.name(), messageService.get(code.getMessageKey()), null);
    }
}
