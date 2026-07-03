package vn.gtel.pm2.sizing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.gtel.pm2.sizing.dto.common.ApiResponse;
import vn.gtel.pm2.sizing.dto.request.PatchUserRequest;
import vn.gtel.pm2.sizing.dto.response.UserResponse;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.i18n.MessageService;
import vn.gtel.pm2.sizing.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {
        return ApiResponse.success(
                HttpStatus.OK,
                ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()),
                userService.getCurrentUserResponse()
        );
    }

    @PatchMapping("/me")
    public ApiResponse<UserResponse> patchCurrentUser(@Valid @RequestBody PatchUserRequest request) {
        return ApiResponse.success(
                HttpStatus.OK,
                ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()),
                userService.patchCurrentUser(request)
        );
    }
}
