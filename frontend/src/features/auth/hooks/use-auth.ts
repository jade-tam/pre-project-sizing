"use client";

import { useCurrentUserQuery } from "@/features/user/queries/use-current-user-query";

export function useAuth() {
  const query = useCurrentUserQuery();

  return {
    user: query.data,
    isAuthenticated: !!query.data,
    isLoading: query.isPending,
  };
}
