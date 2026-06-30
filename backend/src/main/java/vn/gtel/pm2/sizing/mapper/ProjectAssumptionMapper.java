package vn.gtel.pm2.sizing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectAssumptionRequest;
import vn.gtel.pm2.sizing.dto.response.ProjectAssumptionResponse;
import vn.gtel.pm2.sizing.entity.ProjectAssumption;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ProjectAssumptionMapper {
    ProjectAssumptionResponse toResponse(ProjectAssumption entity);

    void updateEntity(UpdateProjectAssumptionRequest request,
                      @MappingTarget ProjectAssumption entity);
}
