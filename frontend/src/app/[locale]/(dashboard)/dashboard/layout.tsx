"use client";

import { getAdminNavConfig } from "@/components/layouts/dashboard/config/admin-nav";
import DashboardShell from "@/components/layouts/dashboard/shell/DashboardShell";
import { PageLoading } from "@/components/page-loading";
import { PageTransitionShell } from "@/components/transitions/page-transition-shell";
import { useRequireAuth } from "@/features/auth/hooks/use-require-auth";
import { useTranslations } from "next-intl";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const t = useTranslations();
  const adminNavItems = getAdminNavConfig(t);

  const { isLoading } = useRequireAuth();

  if (isLoading) {
    return <PageLoading text={t("dashboard.loading")} variant="fullscreen" />;
  }

  return (
    <DashboardShell navItems={adminNavItems}>
      <PageTransitionShell>{children}</PageTransitionShell>
    </DashboardShell>
  );
}
