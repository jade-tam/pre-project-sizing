package vn.gtel.pm2.sizing.dto.common;

import lombok.*;
import org.springframework.http.HttpStatus;
import vn.gtel.pm2.sizing.enums.ResponseCode;

import java.time.Instant;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String code;
    private String message;
    private T data;
    private Map<String, String> errors;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, ResponseCode responseCode, T data) {
        return new ApiResponse<>(
                true,
                status.value(),
                responseCode.getCode(),
                responseCode.getDefaultMessage(),
                data,
                null,
                Instant.now()
        );
    }

    public static <T> ApiResponse<T> error(HttpStatus status, ResponseCode responseCode, Map<String, String> errors) {
        return new ApiResponse<>(
                false,
                status.value(),
                responseCode.getCode(),
                responseCode.getDefaultMessage(),
                null,
                errors,
                Instant.now()
        );
    }
}