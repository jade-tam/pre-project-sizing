import { useQuery } from "@tanstack/react-query";
import { catalogComponentApi } from "../api/catalog-component.api";
import { catalogComponentQueryKeys } from "./query-keys";

export function useCatalogComponentsQuery() {
  return useQuery({
    queryKey: catalogComponentQueryKeys.list(),
    queryFn: () => catalogComponentApi.getCatalogComponents(),
    select: (response) => response.data,
  });
}
