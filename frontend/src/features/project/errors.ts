const PROJECT_ERROR_TRANSLATION_KEYS = {
  not_found: "apiErrors.project.not_found",
  validation_error: "apiErrors.project.validation_error",
  client_error: "apiErrors.project.client_error",
  contract_error: "apiErrors.project.contract_error",
  upstream_error: "apiErrors.project.upstream_error",
  network_error: "apiErrors.project.network_error",
  permission_denied: "apiErrors.project.permission_denied",
  project_request_failed: "apiErrors.project.project_request_failed",
  invalid_request: "apiErrors.common.invalid_request",
  request_failed: "apiErrors.common.request_failed",
} as const;

export function getProjectErrorTranslationKey(
  errorCode: string | undefined,
  fallbackKey:
    | "toast.project.loadFailed"
    | "toast.project.loadListFailed"
    | "toast.project.saveFailed"
    | "toast.project.createFailed"
    | "toast.project.deleteFailed",
) {
  return PROJECT_ERROR_TRANSLATION_KEYS[errorCode as keyof typeof PROJECT_ERROR_TRANSLATION_KEYS] ?? fallbackKey;
}
