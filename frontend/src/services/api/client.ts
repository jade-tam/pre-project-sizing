import axios, { AxiosError, InternalAxiosRequestConfig } from "axios";

import { tokenStorage } from "./token-storage";
import { AuthResponse } from "@/features/auth/types/response/auth-response";
import { clientEnv } from "@/lib/env/client";

interface RetryRequestConfig extends InternalAxiosRequestConfig {
  _retry?: boolean;
}

export const api = axios.create({
  baseURL: clientEnv.NEXT_PUBLIC_REST_API_BASE_URL,
  timeout: 30_000,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config) => {
  const accessToken = tokenStorage.getAccessToken();

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

let refreshPromise: Promise<AuthResponse> | null = null;

api.interceptors.response.use(
  (response) => response,

  async (error: AxiosError) => {
    const originalRequest = error.config as RetryRequestConfig;

    // Ignore retry and move to login if 401 is in auth pages ==============
    const url = originalRequest?.url ?? "";
    const isAuthRequest =
      url.includes("/auth/login") ||
      url.includes("/auth/register") ||
      url.includes("/auth/refresh");

    if (isAuthRequest) {
      return Promise.reject(error);
    }
    // =====================================================================

    if (error.response?.status !== 401 || originalRequest._retry) {
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    try {
      if (!refreshPromise) {
        const refreshToken = tokenStorage.getRefreshToken();

        if (!refreshToken) {
          throw new Error("No refresh token");
        }

        refreshPromise = axios
          .post<AuthResponse>(
            `${process.env.NEXT_PUBLIC_API_URL}/auth/refresh`,
            {
              refreshToken,
            },
          )
          .then(({ data }) => {
            tokenStorage.setTokens(data.accessToken, data.refreshToken);

            return data;
          })
          .finally(() => {
            refreshPromise = null;
          });
      }

      const tokens = await refreshPromise;

      originalRequest.headers.Authorization = `Bearer ${tokens.accessToken}`;

      return api(originalRequest);
    } catch (err) {
      tokenStorage.clear();

      if (typeof window !== "undefined") {
        window.location.href = "/login";
      }

      return Promise.reject(err);
    }
  },
);
