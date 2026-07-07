"use client";

import { use } from "react";
import { useTranslations } from "next-intl";

import { DashboardPageShell } from "@/components/layouts/dashboard/dashboard-page-shell";
import { ProjectDetailsPage } from "@/features/project/components/project-details-page";

export default function ProjectDetailsRoute({
  params,
}: {
  params: Promise<{ locale: string; id: string }>;
}) {
  const t = useTranslations();
  const { id } = use(params);

  return (
    <DashboardPageShell
      title={t("pages.projectDetails.title")}
      description={t("pages.projectDetails.description")}
    >
      <ProjectDetailsPage id={id} />
    </DashboardPageShell>
  );
}
