package vn.gtel.pm2.sizing.enums;

public enum ResponseCode {
    // Success
    SUCCESS,
    ACCOUNT_CREATED,

    // Errors
    VALIDATION_FAILED,
    INTERNAL_SERVER_ERROR,
    USERNAME_ALREADY_EXIST,
    USER_NOT_FOUND,
    INVALID_CREDENTIALS,
    INVALID_REFRESH_TOKEN,
    EMAIL_ALREADY_EXIST;

    public String getMessageKey() {
        return name();
    }
}
