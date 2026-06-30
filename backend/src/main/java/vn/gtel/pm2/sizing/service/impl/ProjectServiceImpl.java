package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gtel.pm2.sizing.dto.common.PaginationResponse;
import vn.gtel.pm2.sizing.dto.query.ProjectQuery;
import vn.gtel.pm2.sizing.dto.request.CreateProjectRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectAssumptionRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectComponentSelectionsRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectInfoRequest;
import vn.gtel.pm2.sizing.dto.response.ProjectResponse;
import vn.gtel.pm2.sizing.entity.CatalogComponent;
import vn.gtel.pm2.sizing.entity.Project;
import vn.gtel.pm2.sizing.entity.ProjectAssumption;
import vn.gtel.pm2.sizing.entity.User;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.exception.ResourceNotFoundException;
import vn.gtel.pm2.sizing.mapper.ProjectAssumptionMapper;
import vn.gtel.pm2.sizing.mapper.ProjectMapper;
import vn.gtel.pm2.sizing.repository.CatalogComponentRepository;
import vn.gtel.pm2.sizing.repository.ProjectRepository;
import vn.gtel.pm2.sizing.service.ProjectService;
import vn.gtel.pm2.sizing.service.UserService;
import vn.gtel.pm2.sizing.specification.ProjectSpecification;
import vn.gtel.pm2.sizing.util.PageableUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectAssumptionMapper projectAssumptionMapper;
    private final CatalogComponentRepository catalogComponentRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<ProjectResponse> getAllProjects(ProjectQuery query) {
        Specification<Project> specification = ProjectSpecification.from(query);
        Pageable pageable = PageableUtils.from(query);

        Page<Project> projectPage = projectRepository.findAll(specification, pageable);
        Page<ProjectResponse> projectResponsePage = projectPage.map(projectMapper::toResponse);

        return PaginationResponse.from(projectResponsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long id) {
        Project foundProject = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));

        return projectMapper.toResponse(foundProject);
    }

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {

        User currentUser = userService.getCurrentUser();

        Project project = new Project(
                currentUser,
                request.getName(),
                request.getDescription(),
                new ArrayList<>(),
                null
        );

        ProjectAssumption projectAssumption = new ProjectAssumption(
                project,
                0,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0,
                0F
        );

        project.setProjectAssumption(projectAssumption);
        projectRepository.save(project);

        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProjectInfo(Long id, UpdateProjectInfoRequest request) {

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));
        projectMapper.updateProjectInfo(request, project);
        projectRepository.save(project);

        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProjectComponentSelections(Long id, UpdateProjectComponentSelectionsRequest request) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));

        // TODO: Need refinement, this business code is not complete
        List<CatalogComponent> catalogComponents = catalogComponentRepository.findAllById(request.getSelectedCatalogComponentIds());
        project.setComponentList(catalogComponents);
        projectRepository.save(project);

        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProjectAssumptions(Long id, UpdateProjectAssumptionRequest request) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));

        projectAssumptionMapper.updateEntity(request, project.getProjectAssumption());
        projectRepository.save(project);

        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public Void deleteProject(Long id) {

        User currentUser = userService.getCurrentUser();

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));
        project.setDeleted(true);
        project.setDeletedBy(currentUser.getId());
        project.setDeletedAt(Instant.now());

        return null;
    }
}
