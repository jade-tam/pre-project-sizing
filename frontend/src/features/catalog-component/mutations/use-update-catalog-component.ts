import { useMutation, useQueryClient } from "@tanstack/react-query";
import { catalogComponentApi } from "../api/catalog-component.api";
import type { UpdateCatalogComponentRequest } from "../types/request/update-catalog-component-request";
import { catalogComponentQueryKeys } from "../queries/query-keys";

export function useUpdateCatalogComponentMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, request }: { id: number; request: UpdateCatalogComponentRequest }) =>
      catalogComponentApi.updateCatalogComponent(id, request),
    onSuccess: async (response) => {
      await queryClient.invalidateQueries({ queryKey: catalogComponentQueryKeys.all });
      queryClient.setQueryData(
        catalogComponentQueryKeys.detail(response.data.id),
        response,
      );
    },
  });
}
