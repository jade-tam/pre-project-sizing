import { useQuery } from "@tanstack/react-query";
import { catalogComponentApi } from "../api/catalog-component.api";
import { catalogComponentQueryKeys } from "./query-keys";

export function useCatalogComponentQuery(id: number) {
  return useQuery({
    queryKey: catalogComponentQueryKeys.detail(id),
    queryFn: () => catalogComponentApi.getCatalogComponent(id),
    select: (response) => response.data,
  });
}
