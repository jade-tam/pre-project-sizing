package vn.gtel.pm2.sizing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.gtel.pm2.sizing.dto.common.ApiResponse;
import vn.gtel.pm2.sizing.dto.common.PaginationResponse;
import vn.gtel.pm2.sizing.dto.query.ProjectQuery;
import vn.gtel.pm2.sizing.dto.request.CreateProjectRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectAssumptionRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectComponentSelectionsRequest;
import vn.gtel.pm2.sizing.dto.request.UpdateProjectInfoRequest;
import vn.gtel.pm2.sizing.dto.response.ProjectResponse;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.i18n.MessageService;
import vn.gtel.pm2.sizing.service.ProjectService;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MessageService messageService;

    @GetMapping
    public ApiResponse<PaginationResponse<ProjectResponse>> getOwnedProjects(@Valid @ModelAttribute ProjectQuery query) {
        return ApiResponse.success(
                HttpStatus.OK,
                ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()),
                projectService.getOwnedProjects(query)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> getOwnedProject(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK, ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()), projectService.getOwnedProject(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        return ApiResponse.success(HttpStatus.CREATED, ResponseCode.PROJECT_CREATED.name(),
                messageService.get(ResponseCode.PROJECT_CREATED.getMessageKey()), projectService.createProject(request));
    }

    @PutMapping("/{id}/info")
    public ApiResponse<ProjectResponse> updateProjectInfo(@PathVariable Long id, @Valid @RequestBody UpdateProjectInfoRequest request) {
        return ApiResponse.success(HttpStatus.OK, ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()), projectService.updateProjectInfo(id, request));
    }

    @PutMapping("/{id}/component-selections")
    public ApiResponse<ProjectResponse> updateProjectComponentSelections(@PathVariable Long id, @Valid @RequestBody UpdateProjectComponentSelectionsRequest request) {
        return ApiResponse.success(HttpStatus.OK, ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()), projectService.updateProjectComponentSelections(id, request));
    }

    @PutMapping("/{id}/assumptions")
    public ApiResponse<ProjectResponse> updateProjectComponentAssumptions(@PathVariable Long id, @Valid @RequestBody UpdateProjectAssumptionRequest request) {
        return ApiResponse.success(HttpStatus.OK, ResponseCode.SUCCESS.name(),
                messageService.get(ResponseCode.SUCCESS.getMessageKey()), projectService.updateProjectAssumptions(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteProject(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.NO_CONTENT, ResponseCode.NO_CONTENT.name(),
                messageService.get(ResponseCode.NO_CONTENT.getMessageKey()), projectService.deleteProject(id));
    }
}
