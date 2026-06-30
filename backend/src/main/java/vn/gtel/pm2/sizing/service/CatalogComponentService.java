package vn.gtel.pm2.sizing.service;

import vn.gtel.pm2.sizing.dto.request.UpdateCatalogComponentRequest;
import vn.gtel.pm2.sizing.dto.response.CatalogComponentResponse;

import java.util.List;

public interface CatalogComponentService {
    List<CatalogComponentResponse> getAllCatalogComponents();

    CatalogComponentResponse getCatalogComponent(Long id);

    CatalogComponentResponse updateCatalogComponent(Long id, UpdateCatalogComponentRequest request);
}
