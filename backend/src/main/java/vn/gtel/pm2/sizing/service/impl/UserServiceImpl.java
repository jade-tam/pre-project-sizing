package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gtel.pm2.sizing.dto.request.PatchUserRequest;
import vn.gtel.pm2.sizing.dto.response.UserResponse;
import vn.gtel.pm2.sizing.entity.User;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.exception.BusinessException;
import vn.gtel.pm2.sizing.exception.ResourceNotFoundException;
import vn.gtel.pm2.sizing.mapper.UserMapper;
import vn.gtel.pm2.sizing.repository.UserRepository;
import vn.gtel.pm2.sizing.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UUID getCurrentUserId() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return UUID.fromString(principal.getSubject());
    }

    @Override
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findById(UUID.fromString(principal.getSubject()))
                .orElseThrow(() ->
                        new ResourceNotFoundException(ResponseCode.USER_NOT_FOUND));
    }

    @Override
    public UserResponse getCurrentUserResponse() {
        User user = getCurrentUser();

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse patchCurrentUser(PatchUserRequest request) {
        log.info("Patching user username={}", request.getUsername());

        User user = getCurrentUser();

        if (!request.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ResponseCode.USERNAME_ALREADY_EXIST);
        }

        userMapper.patchEntity(request, user);
        userRepository.save(user);

        log.info("Patched user username={}", request.getUsername());

        return userMapper.toResponse(user);
    }
}