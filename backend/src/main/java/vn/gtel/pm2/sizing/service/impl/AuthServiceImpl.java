package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gtel.pm2.sizing.constant.AppConstants;
import vn.gtel.pm2.sizing.dto.request.AuthLoginRequest;
import vn.gtel.pm2.sizing.dto.request.AuthRefreshRequest;
import vn.gtel.pm2.sizing.dto.request.AuthRegisterRequest;
import vn.gtel.pm2.sizing.dto.response.AuthResponse;
import vn.gtel.pm2.sizing.entity.User;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.exception.AuthenticationException;
import vn.gtel.pm2.sizing.exception.BusinessException;
import vn.gtel.pm2.sizing.exception.ResourceNotFoundException;
import vn.gtel.pm2.sizing.repository.UserRepository;
import vn.gtel.pm2.sizing.security.CustomUserDetails;
import vn.gtel.pm2.sizing.security.JwtService;
import vn.gtel.pm2.sizing.service.AuthService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${security.jwt.access-token-expiration}")
    private long accessTokenExpirationSeconds;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(AuthRegisterRequest request) {

        log.info("Registering new user username={} email={}", request.getUsername(), request.getEmail());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ResponseCode.USERNAME_ALREADY_EXIST);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ResponseCode.EMAIL_ALREADY_EXIST);
        }

        User savedUser = userRepository.save(
                new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getFullName(), request.getEmail(), true)
        );

        log.info("Registered user username={} email={} with id={}", savedUser.getUsername(), savedUser.getEmail(), savedUser.getId());

        return new AuthResponse(
                jwtService.generateAccessToken(savedUser),
                jwtService.generateRefreshToken(savedUser),
                AppConstants.JWT_TOKEN_TYPE,
                accessTokenExpirationSeconds
        );
    }

    @Override
    @Transactional
    public AuthResponse login(AuthLoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername(); // This method is configured to return email

        User user = userRepository.findByEmail(email).orElseThrow(); // Exceptions handled by authenticationManager authenticate

        return new AuthResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                AppConstants.JWT_TOKEN_TYPE,
                accessTokenExpirationSeconds
        );
    }

    @Override
    public AuthResponse refresh(AuthRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new AuthenticationException(ResponseCode.INVALID_REFRESH_TOKEN);
        }

        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.USER_NOT_FOUND));

        return new AuthResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                AppConstants.JWT_TOKEN_TYPE,
                accessTokenExpirationSeconds
        );
    }

    @Override
    public void logout() {
        // tokens are currently stateless so server doesn't do anything with logout
    }
}
