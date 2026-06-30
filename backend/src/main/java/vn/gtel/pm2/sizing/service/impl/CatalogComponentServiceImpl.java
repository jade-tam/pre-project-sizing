package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gtel.pm2.sizing.dto.request.UpdateCatalogComponentRequest;
import vn.gtel.pm2.sizing.dto.response.CatalogComponentResponse;
import vn.gtel.pm2.sizing.entity.CatalogComponent;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.exception.ResourceNotFoundException;
import vn.gtel.pm2.sizing.mapper.CatalogComponentMapper;
import vn.gtel.pm2.sizing.repository.CatalogComponentRepository;
import vn.gtel.pm2.sizing.service.CatalogComponentService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogComponentServiceImpl implements CatalogComponentService {

    private final CatalogComponentRepository catalogComponentRepository;
    private final CatalogComponentMapper catalogComponentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CatalogComponentResponse> getAllCatalogComponents() {

        return catalogComponentMapper.toResponseList(catalogComponentRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogComponentResponse getCatalogComponent(Long id) {

        CatalogComponent foundCatalogComponent = catalogComponentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.CATALOG_COMPONENT_NOT_FOUND));

        return catalogComponentMapper.toResponse(foundCatalogComponent);
    }

    @Override
    @Transactional
    public CatalogComponentResponse updateCatalogComponent(Long id, UpdateCatalogComponentRequest request) {

        CatalogComponent catalogComponent = catalogComponentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.CATALOG_COMPONENT_NOT_FOUND));
        catalogComponentMapper.updateEntity(request, catalogComponent);
        catalogComponent = catalogComponentRepository.save(catalogComponent);

        return catalogComponentMapper.toResponse(catalogComponent);
    }
}
