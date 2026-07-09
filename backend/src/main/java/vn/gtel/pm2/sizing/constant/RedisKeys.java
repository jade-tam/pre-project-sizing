package vn.gtel.pm2.sizing.constant;

public final class RedisKeys {

    private RedisKeys() {}
    
		// Grouping auth keys
    public static final class Auth {

        public static String loginAttempts(String email) {
            return "auth:login-attempt:" + email;
        }

        public static String registerAttempts(String ip) {
            return "auth:register-attempt:" + ip;
        }

        public static String forgotPassword(String email) {
            return "auth:forgot-password:" + email;
        }

        public static String sendOtp(String phone) {
            return "auth:send-otp:" + phone;
        }
    }

    public static final class Api {

        public static String search(Long userId) {
            return "api:search:" + userId;
        }
    }

    public static final class User {

        public static String profile(Long id) {
            return "user:profile:" + id;
        }
    }
}