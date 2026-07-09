import { showErrorToast, showSuccessToast } from "@/lib/toast/toast";
import { tokenStorage } from "@/services/api/token-storage";
import { ApiResponse } from "@/types/api-response";
import { useMutation } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { authApi } from "../api/auth.api";
import { LoginRequest } from "../types/request/login-request";
import { AuthResponse } from "../types/response/auth-response";
import { AxiosError } from "axios";
import { useTranslations } from "next-intl";

export function useLogin() {
  const router = useRouter();
  const t = useTranslations();

  return useMutation<
    ApiResponse<AuthResponse>, // Success data
    AxiosError<ApiResponse<null>>, // Error data
    LoginRequest // Variables
  >({
    mutationFn: authApi.login,

    onSuccess(response) {
      const { data } = response;

      tokenStorage.setTokens(data.accessToken, data.refreshToken);
      showSuccessToast(t("toast.auth.signedIn"));
      router.push("/dashboard");
    },

    onError: (error) => {
      const { response } = error;
      showErrorToast(
        response?.data.message
          ? `${t("toast.auth.signInFailed")} ${response?.data.message}`
          : t("toast.auth.serviceUnavailable"),
      );
    },
  });
}
