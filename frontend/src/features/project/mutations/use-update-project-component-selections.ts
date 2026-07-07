import { useMutation, useQueryClient } from "@tanstack/react-query";
import { projectApi } from "../api/project.api";
import type { UpdateProjectComponentSelectionsRequest } from "../types/request/update-project-component-selections-request";
import { projectQueryKeys } from "../queries/query-keys";

export function useUpdateProjectComponentSelectionsMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: number; request: UpdateProjectComponentSelectionsRequest }) =>
      projectApi.updateProjectComponentSelections(id, request),
    onSuccess: async (response) => {
      await queryClient.invalidateQueries({ queryKey: projectQueryKeys.all });
      queryClient.setQueryData(
        projectQueryKeys.detail(response.data.id),
        response,
      );
    },
  });
}
