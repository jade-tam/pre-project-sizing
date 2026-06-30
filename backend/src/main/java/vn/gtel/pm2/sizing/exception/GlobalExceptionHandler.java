package vn.gtel.pm2.sizing.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.gtel.pm2.sizing.dto.common.ApiResponse;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.i18n.MessageService;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageService messageService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handle(ResourceNotFoundException ex) {

        ResponseCode code = ex.getResponseCode();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, code.name(), messageService.get(code.getMessageKey()), null)
                );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {

        ResponseCode code = ex.getResponseCode();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT, code.name(), messageService.get(code.getMessageKey()), null)
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ResponseCode code = ResponseCode.VALIDATION_FAILED;

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, code.name(), messageService.get(code.getMessageKey()), errors));
    }

    @ExceptionHandler({
            AuthenticationException.class,
            org.springframework.security.core.AuthenticationException.class,
            JwtException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(Exception ex) {

        ResponseCode code = ResponseCode.INVALID_CREDENTIALS;

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                        HttpStatus.UNAUTHORIZED,
                        code.name(), messageService.get(code.getMessageKey()),
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {

        ResponseCode code = ResponseCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, code.name(), messageService.get(code.getMessageKey()), null));
    }
}