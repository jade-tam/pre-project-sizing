import { useMutation, useQueryClient } from "@tanstack/react-query";
import { projectApi } from "../api/project.api";
import type { CreateProjectRequest } from "../types/request/create-project-request";
import { projectQueryKeys } from "../queries/query-keys";

export function useCreateProjectMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (request: CreateProjectRequest) => projectApi.createProject(request),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: projectQueryKeys.all });
    },
  });
}
