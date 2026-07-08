package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gtel.pm2.sizing.constant.CacheNames;
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
import vn.gtel.pm2.sizing.service.SizingService;
import vn.gtel.pm2.sizing.service.UserService;
import vn.gtel.pm2.sizing.specification.ProjectSpecification;
import vn.gtel.pm2.sizing.util.PageableUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectAssumptionMapper projectAssumptionMapper;
    private final CatalogComponentRepository catalogComponentRepository;
    private final SizingService sizingService;

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<ProjectResponse> getOwnedProjects(ProjectQuery query) {
        UUID userId = userService.getCurrentUserId();

        Specification<Project> specification = Specification
                .where(ProjectSpecification.filters(query))
                .and(ProjectSpecification.ownedBy(userId))
                .and(ProjectSpecification.notDeleted());

        Pageable pageable = PageableUtils.from(query);

        Page<Project> projectPage = projectRepository.findAll(specification, pageable);
        Page<ProjectResponse> projectResponsePage = projectPage.map(this::buildProjectResponse);

        return PaginationResponse.from(projectResponsePage);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(CacheNames.PROJECTS)
    public ProjectResponse getOwnedProject(Long id) {
        UUID currentUserId = userService.getCurrentUserId();

        Project project = projectRepository.findByIdAndOwnerIdAndDeletedFalse(id, currentUserId).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));

        return buildProjectResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {

        log.info("Creating project name={}", request.getName());

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

        log.info("Created project name={} - id={}", project.getName(), project.getId());

        return buildProjectResponse(project);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = CacheNames.PROJECTS,
            key = "#id"
    )
    public ProjectResponse updateProjectInfo(Long id, UpdateProjectInfoRequest request) {

        log.info("Updating info for project id={}", id);

        UUID currentUserId = userService.getCurrentUserId();

        Project project = projectRepository.findByIdAndOwnerIdAndDeletedFalse(id, currentUserId).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));
        projectMapper.updateProjectInfo(request, project);
        projectRepository.save(project);

        log.info("Updated info for project id={}", id);

        return buildProjectResponse(project);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = CacheNames.PROJECTS,
            key = "#id"
    )
    public ProjectResponse updateProjectComponentSelections(Long id, UpdateProjectComponentSelectionsRequest request) {

        log.info("Updating component selections for project id={}", id);

        UUID currentUserId = userService.getCurrentUserId();

        Project project = projectRepository.findByIdAndOwnerIdAndDeletedFalse(id, currentUserId).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));

        List<CatalogComponent> catalogComponents = catalogComponentRepository.findAllByIdInAndIsActiveTrue(request.getSelectedCatalogComponentIds());
        project.setComponentList(catalogComponents);
        projectRepository.save(project);

        log.info("Updated component selections for project id={}", id);

        return buildProjectResponse(project);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = CacheNames.PROJECTS,
            key = "#id"
    )
    public ProjectResponse updateProjectAssumptions(Long id, UpdateProjectAssumptionRequest request) {

        log.info("Updating assumptions for project id={}", id);

        UUID currentUserId = userService.getCurrentUserId();

        Project project = projectRepository.findByIdAndOwnerIdAndDeletedFalse(id, currentUserId).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));
        projectAssumptionMapper.updateEntity(request, project.getProjectAssumption());
        projectRepository.save(project);

        log.info("Updated assumption for project id={}", id);

        return buildProjectResponse(project);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = CacheNames.PROJECTS,
            key = "#id"
    )
    public Void deleteProject(Long id) {
        log.info("Deleting project id={}", id);

        UUID currentUserId = userService.getCurrentUserId();

        Project project = projectRepository.findByIdAndOwnerIdAndDeletedFalse(id, currentUserId).orElseThrow(() -> new ResourceNotFoundException(ResponseCode.PROJECT_NOT_FOUND));
        project.setDeleted(true);
        project.setDeletedBy(currentUserId);
        project.setDeletedAt(Instant.now());

        log.info("Deleted project id={}", id);

        return null;
    }

    // Helper method for reusing code
    private ProjectResponse buildProjectResponse(Project project) {
        ProjectResponse response = projectMapper.toResponse(project);
        response.setSizingResults(sizingService.calculateSizingResults(project));
        response.setTotalMachinesResult(sizingService.calculateTotalMachines(response.getSizingResults()));

        return response;
    }
}
