import { useQuery } from "@tanstack/react-query";
import { projectApi } from "../api/project.api";
import type { ProjectQuery } from "../types/query/project-query";
import { projectQueryKeys } from "./query-keys";

export function useOwnedProjectsQuery(query: ProjectQuery = {}) {
  return useQuery({
    queryKey: projectQueryKeys.list(query),
    queryFn: () => projectApi.getOwnedProjects(query),
    select: (response) => response.data,
  });
}
