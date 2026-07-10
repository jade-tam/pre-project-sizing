package vn.gtel.pm2.sizing.messaging.event;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID id,
        String fullName,
        String email
) {}