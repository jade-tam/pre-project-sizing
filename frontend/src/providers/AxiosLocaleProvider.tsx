"use client";

import { api } from "@/services/api/client";
import { useLocale } from "next-intl";
import { useEffect } from "react";

export function AxiosLocaleProvider({ children }: { children: React.ReactNode }) {
  const locale = useLocale();

  useEffect(() => {
    api.defaults.headers.common["Accept-Language"] = locale;
  }, [locale]);

  return children;
}
