import { useMutation, useQueryClient } from "@tanstack/react-query";
import { projectApi } from "../api/project.api";
import type { UpdateProjectAssumptionRequest } from "../types/request/update-project-assumption-request";
import { projectQueryKeys } from "../queries/query-keys";

export function useUpdateProjectAssumptionsMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      request,
    }: {
      id: number;
      request: UpdateProjectAssumptionRequest;
    }) => projectApi.updateProjectAssumptions(id, request),
    onSuccess: async (response) => {
      await queryClient.invalidateQueries({ queryKey: projectQueryKeys.all });
      queryClient.setQueryData(
        projectQueryKeys.detail(response.data.id),
        response,
      );
    },
  });
}
