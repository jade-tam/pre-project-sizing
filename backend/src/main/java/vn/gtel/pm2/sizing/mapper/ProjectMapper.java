package vn.gtel.pm2.sizing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectInfoRequest;
import vn.gtel.pm2.sizing.dto.response.ProjectResponse;
import vn.gtel.pm2.sizing.entity.Project;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                CatalogComponentMapper.class,
                ProjectAssumptionMapper.class,
                UserMapper.class
        }
)
public interface ProjectMapper {
    ProjectResponse toResponse(Project entity);

    void updateProjectInfo(UpdateProjectInfoRequest request,
                           @MappingTarget Project entity);

}
