import { useMutation } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { authApi } from "../api/auth.api";
import { tokenStorage } from "@/services/api/token-storage";
import { useTranslations } from "next-intl";
import { showSuccessToast } from "@/lib/toast/toast";

export function useLogout() {
  const router = useRouter();
  const t = useTranslations();

  return useMutation({
    mutationFn: authApi.logout,

    onSettled() {
      tokenStorage.clear();
      showSuccessToast(t("toast.auth.logoutSuccess"));

      router.replace("/login");
    },
  });
}
