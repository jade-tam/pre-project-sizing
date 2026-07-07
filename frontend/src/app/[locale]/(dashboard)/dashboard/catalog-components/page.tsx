"use client";

import { useTranslations } from "next-intl";

import { DashboardPageShell } from "@/components/layouts/dashboard/dashboard-page-shell";
import { CatalogComponentsPage } from "@/features/catalog-component/components/catalog-components-page";

export default function CatalogComponentsRoute() {
  const t = useTranslations();

  return (
    <DashboardPageShell
      title={t("pages.catalogComponents.title")}
      description={t("pages.catalogComponents.description")}
      fullWidth
    >
      <CatalogComponentsPage />
    </DashboardPageShell>
  );
}
