package vn.gtel.pm2.sizing.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {
    private UUID id;
    private String fullName;
    private String email;
}