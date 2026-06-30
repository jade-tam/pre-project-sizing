package vn.gtel.pm2.sizing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.gtel.pm2.sizing.dto.common.ApiResponse;
import vn.gtel.pm2.sizing.dto.request.UpdateCatalogComponentRequest;
import vn.gtel.pm2.sizing.dto.response.CatalogComponentResponse;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.i18n.MessageService;
import vn.gtel.pm2.sizing.service.CatalogComponentService;

import java.util.List;

@RestController
@RequestMapping("/catalog-components")
@RequiredArgsConstructor
public class CatalogComponentController {

    private final CatalogComponentService catalogComponentService;
    private final MessageService messageService;

    @GetMapping
    public ApiResponse<List<CatalogComponentResponse>> getAllCatalogComponents() {
        return ApiResponse.success(
                HttpStatus.OK,
                ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()),
                catalogComponentService.getAllCatalogComponents()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<CatalogComponentResponse> getCatalogComponent(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK, ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()), catalogComponentService.getCatalogComponent(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<CatalogComponentResponse> updateCatalogComponent(@PathVariable Long id, @Valid @RequestBody UpdateCatalogComponentRequest request) {
        return ApiResponse.success(HttpStatus.OK, ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()), catalogComponentService.updateCatalogComponent(id, request));
    }
}
