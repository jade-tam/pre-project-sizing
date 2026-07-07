import { useMutation, useQueryClient } from "@tanstack/react-query";
import { projectApi } from "../api/project.api";
import type { UpdateProjectInfoRequest } from "../types/request/update-project-info-request";
import { projectQueryKeys } from "../queries/query-keys";

export function useUpdateProjectInfoMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: number; request: UpdateProjectInfoRequest }) =>
      projectApi.updateProjectInfo(id, request),
    onSuccess: async (response) => {
      await queryClient.invalidateQueries({ queryKey: projectQueryKeys.all });
      queryClient.setQueryData(projectQueryKeys.detail(response.data.id), response.data);
    },
  });
}
