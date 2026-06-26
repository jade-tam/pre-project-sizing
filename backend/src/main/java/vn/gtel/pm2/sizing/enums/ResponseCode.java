package vn.gtel.pm2.sizing.enums;

public enum ResponseCode {
    SUCCESS("SUCCESS", "Thành công"),
    VALIDATION_FAILED("VALIDATION_FAILED", "Dữ liệu không đúng cấu trúc"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Lỗi hệ thống");

    private String code;
    private String defaultMessage;

    ResponseCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
