import { useQuery } from "@tanstack/react-query";
import { projectApi } from "../api/project.api";
import { projectQueryKeys } from "./query-keys";

export function useOwnedProjectQuery(id: number) {
  return useQuery({
    queryKey: projectQueryKeys.detail(id),
    queryFn: () => projectApi.getOwnedProject(id),
    select: (response) => response.data,
  });
}
