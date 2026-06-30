package vn.gtel.pm2.sizing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import vn.gtel.pm2.sizing.dto.request.UpdateCatalogComponentRequest;
import vn.gtel.pm2.sizing.dto.response.CatalogComponentResponse;
import vn.gtel.pm2.sizing.entity.CatalogComponent;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CatalogComponentMapper {
    CatalogComponentResponse toResponse(CatalogComponent entity);

    List<CatalogComponentResponse> toResponseList(List<CatalogComponent> entities);

    void updateEntity(UpdateCatalogComponentRequest request,
                      @MappingTarget CatalogComponent entity);
}
