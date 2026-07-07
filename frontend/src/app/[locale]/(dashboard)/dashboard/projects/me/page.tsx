"use client";

import { useTranslations } from "next-intl";

import { DashboardPageShell } from "@/components/layouts/dashboard/dashboard-page-shell";
import { ProjectOwnedProjectsPage } from "@/features/project/components/project-owned-projects-page";

export default function OwnedProjectsPage() {
  const t = useTranslations();

  return (
    <DashboardPageShell
      title={t("pages.projectsMe.title")}
      description={t("pages.projectsMe.description")}
    >
      <ProjectOwnedProjectsPage />
    </DashboardPageShell>
  );
}
