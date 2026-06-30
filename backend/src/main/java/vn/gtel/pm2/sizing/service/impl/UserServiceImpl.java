package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.gtel.pm2.sizing.entity.User;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.exception.ResourceNotFoundException;
import vn.gtel.pm2.sizing.repository.UserRepository;
import vn.gtel.pm2.sizing.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ResponseCode.USER_NOT_FOUND));
    }
}