"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

import { useCurrentUserQuery } from "@/features/user/queries/use-current-user-query";

export function useRequireRole(roles: string[]) {
  const router = useRouter();

  const { data: currentUser, isPending, isError } = useCurrentUserQuery();

  useEffect(() => {
    if (isPending || isError || !currentUser) {
      return;
    }

    if (!roles.includes(currentUser.role)) {
      router.replace("/unauthorized");
    }
  }, [currentUser, isPending, isError, roles, router]);

  return {
    currentUser,
    isLoading: isPending,
  };
}
