"use client";

import { useCurrentUserQuery } from "@/features/user/queries/use-current-user-query";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export function useRequireAuth() {
  const router = useRouter();

  const { isPending, isError } = useCurrentUserQuery();

  useEffect(() => {
    if (!isPending && isError) {
      router.replace("/login");
    }
  }, [isPending, isError, router]);

  return {
    isLoading: isPending,
  };
}
