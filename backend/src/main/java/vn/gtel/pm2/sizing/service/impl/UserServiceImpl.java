package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.gtel.pm2.sizing.dto.response.UserResponse;
import vn.gtel.pm2.sizing.entity.User;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.exception.ResourceNotFoundException;
import vn.gtel.pm2.sizing.mapper.UserMapper;
import vn.gtel.pm2.sizing.repository.UserRepository;
import vn.gtel.pm2.sizing.service.UserService;

import java.util.UUID;

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
}