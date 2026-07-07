import { api } from "@/services/api/client";
import { ApiResponse } from "@/types/api-response";
import type { CatalogComponentResponse } from "../types/response/catalog-component-response";
import type { UpdateCatalogComponentRequest } from "../types/request/update-catalog-component-request";

export const catalogComponentApi = {
  async getCatalogComponents(): Promise<ApiResponse<CatalogComponentResponse[]>> {
    const { data } = await api.get<ApiResponse<CatalogComponentResponse[]>>("/catalog-components");

    return data;
  },

  async getCatalogComponent(id: number): Promise<ApiResponse<CatalogComponentResponse>> {
    const { data } = await api.get<ApiResponse<CatalogComponentResponse>>(`/catalog-components/${id}`);

    return data;
  },

  async updateCatalogComponent(
    id: number,
    request: UpdateCatalogComponentRequest,
  ): Promise<ApiResponse<CatalogComponentResponse>> {
    const { data } = await api.put<ApiResponse<CatalogComponentResponse>>(
      `/catalog-components/${id}`,
      request,
    );

    return data;
  },
};
