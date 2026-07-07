import { api } from "@/services/api/client";
import { AuthResponse } from "../types/response/auth-response";
import { LoginRequest } from "../types/request/login-request";
import { RegisterRequest } from "../types/request/register-request";
import { ApiResponse } from "@/types/api-response";

export const authApi = {
  async login(request: LoginRequest): Promise<ApiResponse<AuthResponse>> {
    const { data } = await api.post<ApiResponse<AuthResponse>>(
      "/auth/login",
      request,
    );

    return data;
  },

  async register(request: RegisterRequest): Promise<ApiResponse<AuthResponse>> {
    const { data } = await api.post<ApiResponse<AuthResponse>>(
      "/auth/register",
      request,
    );

    return data;
  },

  async logout() {
    await api.post("/auth/logout");
  },
};
