package vn.gtel.pm2.sizing.dto.common;

import lombok.*;
import org.springframework.http.HttpStatus;
import vn.gtel.pm2.sizing.enums.ResponseCode;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String code;
    private String message;
    private T data;
    private Map<String, String> errors;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, String code, String message, T data) {
        return new ApiResponse<>(
                true,
                status.value(),
                code,
                message,
                data,
                null,
                Instant.now()
        );
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String code, String message, Map<String, String> errors) {
        return new ApiResponse<>(
                false,
                status.value(),
                code,
                message,
                null,
                errors,
                Instant.now()
        );
    }
}