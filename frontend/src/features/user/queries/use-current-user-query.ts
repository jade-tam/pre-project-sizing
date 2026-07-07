import { useQuery } from "@tanstack/react-query";
import { userQueryKeys } from "./query-keys";
import { userApi } from "../api/user.api";

export function useCurrentUserQuery() {
  return useQuery({
    queryKey: userQueryKeys.current(),

    queryFn: userApi.getCurrentUser,

    select: (response) => response.data,

    staleTime: 5 * 60 * 1000,
  });
}
