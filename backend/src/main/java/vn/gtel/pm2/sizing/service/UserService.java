package vn.gtel.pm2.sizing.service;

import vn.gtel.pm2.sizing.entity.User;

import java.util.UUID;

public interface UserService {
    UUID getCurrentUserId();
    User getCurrentUser();
}
