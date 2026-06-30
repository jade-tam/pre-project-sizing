package vn.gtel.pm2.sizing.constant;

public final class AppConstants {
    public static final String JWT_TOKEN_TYPE = "Bearer";
    public static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,128}$";
    public static final String SECURITY_SCHEME_NAME = "Bearer Authentication";
}
