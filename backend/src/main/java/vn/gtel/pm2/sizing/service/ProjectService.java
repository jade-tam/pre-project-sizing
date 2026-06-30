package vn.gtel.pm2.sizing.service;

import vn.gtel.pm2.sizing.dto.common.PaginationResponse;
import vn.gtel.pm2.sizing.dto.query.ProjectQuery;
import vn.gtel.pm2.sizing.dto.request.CreateProjectRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectAssumptionRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectComponentSelectionsRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectInfoRequest;
import vn.gtel.pm2.sizing.dto.response.ProjectResponse;

public interface ProjectService {
    PaginationResponse<ProjectResponse> getAllProjects(ProjectQuery query);

    ProjectResponse getProject(Long id);

    ProjectResponse createProject(CreateProjectRequest request);

    ProjectResponse updateProjectInfo(Long id, UpdateProjectInfoRequest request);

    ProjectResponse updateProjectComponentSelections(Long id, UpdateProjectComponentSelectionsRequest request);

    ProjectResponse updateProjectAssumptions(Long id, UpdateProjectAssumptionRequest request);

    Void deleteProject(Long id);
}
