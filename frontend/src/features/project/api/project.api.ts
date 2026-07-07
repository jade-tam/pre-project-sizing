import { api } from "@/services/api/client";
import { ApiResponse } from "@/types/api-response";
import type { CreateProjectRequest } from "../types/request/create-project-request";
import type { UpdateProjectInfoRequest } from "../types/request/update-project-info-request";
import type { UpdateProjectComponentSelectionsRequest } from "../types/request/update-project-component-selections-request";
import type { UpdateProjectAssumptionRequest } from "../types/request/update-project-assumption-request";
import type { PaginationResponse } from "../types/response/pagination-response";
import type { ProjectResponse } from "../types/response/project-response";
import type { ProjectQuery } from "../types/query/project-query";

export const projectApi = {
  async getOwnedProjects(query: ProjectQuery = {}): Promise<ApiResponse<PaginationResponse<ProjectResponse>>> {
    const { data } = await api.get<ApiResponse<PaginationResponse<ProjectResponse>>>("/projects", {
      params: query,
    });

    return data;
  },

  async getOwnedProject(id: number): Promise<ApiResponse<ProjectResponse>> {
    const { data } = await api.get<ApiResponse<ProjectResponse>>(`/projects/${id}`);

    return data;
  },

  async createProject(request: CreateProjectRequest): Promise<ApiResponse<ProjectResponse>> {
    const { data } = await api.post<ApiResponse<ProjectResponse>>("/projects", request);

    return data;
  },

  async deleteProject(id: number): Promise<void> {
    await api.delete(`/projects/${id}`);
  },

  async updateProjectInfo(id: number, request: UpdateProjectInfoRequest): Promise<ApiResponse<ProjectResponse>> {
    const { data } = await api.put<ApiResponse<ProjectResponse>>(`/projects/${id}/info`, request);

    return data;
  },

  async updateProjectComponentSelections(
    id: number,
    request: UpdateProjectComponentSelectionsRequest,
  ): Promise<ApiResponse<ProjectResponse>> {
    const { data } = await api.put<ApiResponse<ProjectResponse>>(
      `/projects/${id}/component-selections`,
      request,
    );

    return data;
  },

  async updateProjectAssumptions(
    id: number,
    request: UpdateProjectAssumptionRequest,
  ): Promise<ApiResponse<ProjectResponse>> {
    const { data } = await api.put<ApiResponse<ProjectResponse>>(`/projects/${id}/assumptions`, request);

    return data;
  },
};
