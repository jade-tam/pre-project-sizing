package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gtel.pm2.sizing.constant.CacheNames;
import vn.gtel.pm2.sizing.dto.request.UpdateCatalogComponentRequest;
import vn.gtel.pm2.sizing.dto.response.CatalogComponentResponse;
import vn.gtel.pm2.sizing.entity.CatalogComponent;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.exception.ResourceNotFoundException;
import vn.gtel.pm2.sizing.mapper.CatalogComponentMapper;
import vn.gtel.pm2.sizing.messaging.event.UserRegisteredEvent;
import vn.gtel.pm2.sizing.messaging.publisher.UserEventPublisher;
import vn.gtel.pm2.sizing.repository.CatalogComponentRepository;
import vn.gtel.pm2.sizing.service.CatalogComponentService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogComponentServiceImpl implements CatalogComponentService {

    private final CatalogComponentRepository catalogComponentRepository;
    private final CatalogComponentMapper catalogComponentMapper;
    private final UserEventPublisher userEventPublisher;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheNames.CATALOG_COMPONENTS, key = "'all'")
    public List<CatalogComponentResponse> getAllCatalogComponents() {
        userEventPublisher.publishUserRegistered(new UserRegisteredEvent(new UUID(234, 234L), "TEST", "asd@gmail.com"));

        return catalogComponentMapper.toResponseList(catalogComponentRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheNames.CATALOG_COMPONENTS)
    public CatalogComponentResponse getCatalogComponent(Long id) {

        CatalogComponent foundCatalogComponent = catalogComponentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.CATALOG_COMPONENT_NOT_FOUND));

        return catalogComponentMapper.toResponse(foundCatalogComponent);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheNames.CATALOG_COMPONENTS, key = "#id"),
            @CacheEvict(value = CacheNames.CATALOG_COMPONENTS, key = "'all'")
    })
    public CatalogComponentResponse updateCatalogComponent(Long id, UpdateCatalogComponentRequest request) {

        log.info("Updating catalog component with id {}", id);

        CatalogComponent catalogComponent = catalogComponentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.CATALOG_COMPONENT_NOT_FOUND));
        catalogComponentMapper.updateEntity(request, catalogComponent);
        catalogComponent = catalogComponentRepository.save(catalogComponent);

        log.info("Updated catalog component with id {}", id);

        return catalogComponentMapper.toResponse(catalogComponent);
    }
}
