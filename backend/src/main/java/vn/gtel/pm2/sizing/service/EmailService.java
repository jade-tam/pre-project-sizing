package vn.gtel.pm2.sizing.service;

import vn.gtel.pm2.sizing.messaging.event.UserRegisteredEvent;

public interface EmailService {
    void sendWelcomeEmail(UserRegisteredEvent event);
}
