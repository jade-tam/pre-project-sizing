import { api } from "@/services/api/client";
import { ApiResponse } from "@/types/api-response";
import { CurrentUser } from "../types/current-user";
import { PatchUserRequest } from "../types/request/patch-user-request";

export const userApi = {
  async getCurrentUser(): Promise<ApiResponse<CurrentUser>> {
    const { data } = await api.get<ApiResponse<CurrentUser>>("/users/me");

    return data;
  },
  async patchCurrentUser(
    updatedUser: Partial<PatchUserRequest>,
  ): Promise<ApiResponse<CurrentUser>> {
    const { data } = await api.patch<ApiResponse<CurrentUser>>(
      "/users/me",
      updatedUser,
    );

    return data;
  },
};
