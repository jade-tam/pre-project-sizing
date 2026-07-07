import { useMutation } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { authApi } from "../api/auth.api";
import { tokenStorage } from "@/services/api/token-storage";
import { ApiResponse } from "@/types/api-response";
import { AxiosError } from "axios";
import { RegisterRequest } from "../types/request/register-request";
import { showErrorToast, showSuccessToast } from "@/lib/toast/toast";
import { AuthResponse } from "../types/response/auth-response";
import { useTranslations } from "next-intl";

export function useRegister() {
  const router = useRouter();
  const t = useTranslations();

  return useMutation<
    ApiResponse<AuthResponse>, // Success data
    AxiosError<ApiResponse<null>>, // Error data
    RegisterRequest // Variables
  >({
    mutationFn: authApi.register,

    onSuccess(response) {
      const { data } = response;

      tokenStorage.setTokens(data.accessToken, data.refreshToken);
      showSuccessToast(t("apiErrors.auth.accountCreated"));
      router.push("/dashboard");
    },

    onError: (error) => {
      const { response } = error;
      showErrorToast(
        response?.data.message
          ? `${t("apiErrors.auth.register_failed")} ${response?.data.message}`
          : t("apiErrors.auth.upstream_service_error"),
      );
    },
  });
}
