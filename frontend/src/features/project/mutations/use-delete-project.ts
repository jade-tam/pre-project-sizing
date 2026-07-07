import { useMutation, useQueryClient } from "@tanstack/react-query";
import { projectApi } from "../api/project.api";
import { projectQueryKeys } from "../queries/query-keys";

export function useDeleteProjectMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: number) => projectApi.deleteProject(id),
    onSuccess: async (_, id) => {
      await queryClient.invalidateQueries({ queryKey: projectQueryKeys.all });
      queryClient.removeQueries({ queryKey: projectQueryKeys.detail(id) });
    },
  });
}
