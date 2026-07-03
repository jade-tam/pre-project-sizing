package vn.gtel.pm2.sizing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Setter
    @Column(name = "password", nullable = false)
    private String passwordHash;

    @Setter
    @Column(name = "full_name", length = 200)
    private String fullName;

    @Setter
    @Column(name = "email", unique = true, length = 120)
    private String email;

    @Setter
    @Column(name = "active")
    private boolean active = false;

    @Setter
    private String avatarUrl;

    @Setter
    @Column(length = 100)
    private String displayName;

    @Setter
    @Column(length = 50)
    private String pronouns;

    @Setter
    @Column(length = 500)
    private String bio;

    public User(String username, String passwordHash, String fullName, String email, boolean active) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.active = active;
        this.avatarUrl = null;
        this.displayName = fullName;
        this.pronouns = "";
        this.bio = "";
    }
}
