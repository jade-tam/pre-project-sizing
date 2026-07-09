"use client";

import { useRouter } from "@/i18n/navigation";
import { useTranslations } from "next-intl";
import { useState } from "react";

import { EmptyPlaceholder } from "@/components/ui/EmptyPlaceholder";
import { useCatalogComponentsQuery } from "@/features/catalog-component/queries/use-catalog-components-query";
import { getProjectErrorTranslationKey } from "@/features/project/errors";
import { useDeleteProjectMutation } from "@/features/project/mutations/use-delete-project";
import { useUpdateProjectAssumptionsMutation } from "@/features/project/mutations/use-update-project-assumptions";
import { useUpdateProjectComponentSelectionsMutation } from "@/features/project/mutations/use-update-project-component-selections";
import { useUpdateProjectInfoMutation } from "@/features/project/mutations/use-update-project-info";
import { useOwnedProjectQuery } from "@/features/project/queries/use-owned-project-query";
import { showErrorToast, showSuccessToast } from "@/lib/toast/toast";
import { ProjectAssumptionsDialog } from "./project-assumptions-dialog";
import { ProjectBasicInfoDialog } from "./project-basic-info-dialog";
import { ProjectComponentSelectionDialog } from "./project-component-selection-dialog";
import { ProjectDeleteDialog } from "./project-delete-dialog";

function formatDate(value: string) {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return "-";
  }

  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();

  return `${hours}:${minutes} ${day}/${month}/${year}`;
}

function formatNumber(value: number) {
  return new Intl.NumberFormat(undefined, { maximumFractionDigits: 2 }).format(
    value,
  );
}

export function ProjectDetailsPage({ id }: { id: string }) {
  const t = useTranslations();
  const router = useRouter();
  const projectId = Number(id);
  const projectQuery = useOwnedProjectQuery(projectId);
  const catalogComponentsQuery = useCatalogComponentsQuery();
  const updateSelectionMutation = useUpdateProjectComponentSelectionsMutation();
  const updateAssumptionsMutation = useUpdateProjectAssumptionsMutation();
  const updateInfoMutation = useUpdateProjectInfoMutation();
  const deleteProjectMutation = useDeleteProjectMutation();
  const [basicInfoOpen, setBasicInfoOpen] = useState(false);
  const [selectionOpen, setSelectionOpen] = useState(false);
  const [assumptionsOpen, setAssumptionsOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);

  if (projectQuery.isPending) {
    return <div className="skeleton h-64 w-full" />;
  }

  if (projectQuery.isError) {
    return (
      <div className="alert alert-error">
        {t(
          getProjectErrorTranslationKey(
            projectQuery.error.message,
            "toast.project.loadFailed",
          ),
        )}
      </div>
    );
  }

  if (!projectQuery.data) {
    return <div className="alert">{t("apiErrors.project.not_found")}</div>;
  }

  const project = projectQuery.data;
  const assumptionEntries = [
    ["concurrentUsers", project.projectAssumption?.concurrentUsers],
    ["headroom", project.projectAssumption?.headroom],
    [
      "requestsPerUserPerSecond",
      project.projectAssumption?.requestsPerUserPerSecond,
    ],
    ["apiCallsPerRequest", project.projectAssumption?.apiCallsPerRequest],
    ["dbRatioPerRequest", project.projectAssumption?.dbRatioPerRequest],
    ["searchRatioPerRequest", project.projectAssumption?.searchRatioPerRequest],
    ["cacheRatioPerRequest", project.projectAssumption?.cacheRatioPerRequest],
    ["kafkaRatioPerRequest", project.projectAssumption?.kafkaRatioPerRequest],
    ["logBytesPerRequest", project.projectAssumption?.logBytesPerRequest],
    ["authRatio", project.projectAssumption?.authRatio],
  ] as const;
  const assumptionTooltips = {
    concurrentUsers: t(
      "pages.projectDetails.assumptions.tooltips.concurrentUsers",
    ),
    headroom: t("pages.projectDetails.assumptions.tooltips.headroom"),
    requestsPerUserPerSecond: t(
      "pages.projectDetails.assumptions.tooltips.requestsPerUserPerSecond",
    ),
    apiCallsPerRequest: t(
      "pages.projectDetails.assumptions.tooltips.apiCallsPerRequest",
    ),
    dbRatioPerRequest: t(
      "pages.projectDetails.assumptions.tooltips.dbRatioPerRequest",
    ),
    searchRatioPerRequest: t(
      "pages.projectDetails.assumptions.tooltips.searchRatioPerRequest",
    ),
    cacheRatioPerRequest: t(
      "pages.projectDetails.assumptions.tooltips.cacheRatioPerRequest",
    ),
    kafkaRatioPerRequest: t(
      "pages.projectDetails.assumptions.tooltips.kafkaRatioPerRequest",
    ),
    logBytesPerRequest: t(
      "pages.projectDetails.assumptions.tooltips.logBytesPerRequest",
    ),
    authRatio: t("pages.projectDetails.assumptions.tooltips.authRatio"),
  } as const;
  const hasAssumptions = project.projectAssumption !== null;
  const selectedCatalogComponentsById = new Map(
    project.selectedCatalogComponents.map((component) => [
      component.id,
      component,
    ]),
  );

  return (
    <div className="space-y-4">
      <section className="grid gap-4 lg:grid-cols-[minmax(0,1fr)_320px]">
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-4">
            <div className="flex items-center justify-between gap-4">
              <span className="badge badge-sm badge-soft badge-primary uppercase tracking-[0.2em]">
                {t("pages.projectDetails.eyebrow", { id: project.id })}
              </span>
              <button
                type="button"
                className="btn btn-primary btn-sm"
                onClick={() => setBasicInfoOpen(true)}
              >
                {t("pages.projectDetails.actions.edit")}
              </button>
            </div>

            <div className="space-y-2">
              <h2 className="text-3xl font-semibold tracking-tight text-base-content sm:text-4xl ">
                {project.name}
              </h2>
              <p className="max-w-2xl text-sm text-base-content/70 sm:text-base">
                {project.description}
              </p>
            </div>

            <div className="flex flex-wrap items-center gap-x-6 gap-y-2 text-sm text-base-content/80">
              <div className="flex items-center gap-2">
                <span
                  className="icon-[fluent--person-20-regular] size-4"
                  aria-hidden="true"
                />
                <span>
                  {t("pages.projectDetails.owner", {
                    name: project.owner.fullName,
                  })}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <span
                  className="icon-[fluent--calendar-ltr-20-regular] size-4"
                  aria-hidden="true"
                />
                <span>
                  {t("pages.projectDetails.updatedAt", {
                    date: formatDate(project.updatedAt),
                  })}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <span
                  className="icon-[fluent--calendar-ltr-20-regular] size-4"
                  aria-hidden="true"
                />
                <span>
                  {t("pages.projectDetails.createdAt", {
                    date: formatDate(project.createdAt),
                  })}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="card border border-base-300 bg-neutral text-neutral-content shadow-sm">
          <div className="card-body items-center justify-center gap-3 text-center">
            <div className="text-xs font-semibold uppercase tracking-[0.28em] text-neutral-content/70">
              {t("pages.projectDetails.totalMachines.eyebrow")}
            </div>
            <div className="text-5xl font-bold leading-none">
              {project.totalMachinesResult}
            </div>
            <div className="text-xl font-semibold leading-tight">
              {t("pages.projectDetails.totalMachines.title")}
            </div>
            <div className="divider my-0 border-neutral-content/20" />
          </div>
        </div>
      </section>

      <section className="grid gap-4 lg:grid-cols-[minmax(0,1fr)_2fr]">
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-4">
            <div className="flex items-center justify-between gap-4">
              <h3 className="card-title text-xl">
                {t("pages.projectDetails.selection.title")}
              </h3>
              <button
                type="button"
                className="btn btn-primary btn-sm"
                onClick={() => setSelectionOpen(true)}
              >
                {t("pages.projectDetails.actions.edit")}
              </button>
            </div>
            <div className="flex flex-wrap gap-2">
              {!project.selectedCatalogComponents.length ? (
                <EmptyPlaceholder
                  label={t("pages.projectDetails.selection.empty")}
                />
              ) : (
                project.selectedCatalogComponents.map((component) => (
                  <span key={component.id} className="badge badge-soft badge">
                    {component.name}
                  </span>
                ))
              )}
            </div>
          </div>
        </div>

        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-4">
            <div className="flex items-center justify-between gap-4">
              <h3 className="card-title text-xl">
                {t("pages.projectDetails.assumptions.title")}
              </h3>
              <button
                type="button"
                className="btn btn-primary btn-sm"
                onClick={() => setAssumptionsOpen(true)}
              >
                {t("pages.projectDetails.actions.edit")}
              </button>
            </div>

            {hasAssumptions ? (
              <div className="grid grid-cols-2 gap-3 md:grid-cols-2 xl:grid-cols-5">
                {assumptionEntries.map(([key, value]) => (
                  <div
                    key={key}
                    className="rounded-field border-base-300 bg-base-200/60 p-3"
                  >
                    <div className="text-[0.6rem] h-10 font-semibold uppercase tracking-[0.18em] text-base-content/80">
                      <span
                        className="tooltip"
                        data-tip={assumptionTooltips[key]}
                      >
                        <span className="inline-flex items-center gap-1">
                          {t(`pages.projectDetails.assumptions.fields.${key}`)}
                          <span
                            className="icon-[fluent--info-20-regular] size-3.5 shrink-0"
                            aria-hidden="true"
                          />
                        </span>
                      </span>
                    </div>
                    <div className="mt-1 text-base font-semibold tabular-nums">
                      {value ?? "-"}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-sm text-base-content/70">
                {t("pages.projectDetails.assumptions.empty")}
              </div>
            )}
          </div>
        </div>
      </section>

      <section className="card border border-base-300 bg-base-100 shadow-sm">
        <div className="card-body gap-4">
          <h3 className="card-title text-xl">
            {t("pages.projectDetails.sizingResults.title")}
          </h3>
          <div className="overflow-x-auto rounded-field border-base-300">
            <table className="table table-sm">
              <thead>
                <tr>
                  <th>
                    {t("pages.projectDetails.sizingResults.columns.component")}
                  </th>
                  <th>
                    {t(
                      "pages.projectDetails.sizingResults.columns.machineSpec",
                    )}
                  </th>
                  <th className="text-right">
                    {t(
                      "pages.projectDetails.sizingResults.columns.perMachineCapacity",
                    )}
                  </th>
                  <th>
                    {t(
                      "pages.projectDetails.sizingResults.columns.capacityUnit",
                    )}
                  </th>
                  <th className="text-right">
                    {t(
                      "pages.projectDetails.sizingResults.columns.requiredCapacity",
                    )}
                  </th>
                  <th className="text-right">
                    {t(
                      "pages.projectDetails.sizingResults.columns.requiredMachines",
                    )}
                  </th>
                  <th>
                    {t("pages.projectDetails.sizingResults.columns.haMinimum")}
                  </th>
                  <th className="text-right">
                    {t(
                      "pages.projectDetails.sizingResults.columns.totalMachines",
                    )}
                  </th>
                  <th className="text-right">
                    {t("pages.projectDetails.sizingResults.columns.component")}
                  </th>
                </tr>
              </thead>
              <tbody>
                {!project.sizingResults.length ? (
                  <tr>
                    <td colSpan={100}>
                      <EmptyPlaceholder
                        label={t("pages.projectDetails.selection.empty")}
                      />
                    </td>
                  </tr>
                ) : (
                  project.sizingResults
                    .sort((a, b) => a.catalogComponentId - b.catalogComponentId)
                    .map((result) => {
                      const selectedComponent =
                        selectedCatalogComponentsById.get(
                          result.catalogComponentId,
                        );

                      return (
                        <tr key={result.catalogComponentKey}>
                          <td className="font-medium">
                            {selectedComponent?.name ??
                              result.catalogComponentName}
                          </td>
                          <td className="">
                            {selectedComponent?.machineSpec ?? "-"}
                          </td>
                          <td className="text-right tabular-nums">
                            {selectedComponent
                              ? formatNumber(
                                  selectedComponent.perMachineCapacity,
                                )
                              : "-"}
                          </td>
                          <td>
                            <div
                              className="tooltip tooltip-right"
                              data-tip={
                                selectedComponent?.capacityUnitDescription
                              }
                            >
                              <div className="flex items-center gap-1">
                                {result.catalogComponentCapacityUnit}
                                <span
                                  className="icon-[fluent--info-20-regular] size-3.5 shrink-0"
                                  aria-hidden="true"
                                />
                              </div>
                            </div>
                          </td>
                          <td className="text-right tabular-nums">
                            {formatNumber(result.requiredCapacity)}
                          </td>
                          <td className="text-right tabular-nums">
                            {result.requiredMachines}
                          </td>
                          <td className="text-right tabular-nums">
                            {selectedComponent?.haMinimum ?? "-"}
                          </td>
                          <td className="text-right tabular-nums  font-semibold">
                            {result.totalMachines}
                          </td>
                          <td className="font-medium">
                            {selectedComponent?.name ??
                              result.catalogComponentName}
                          </td>
                        </tr>
                      );
                    })
                )}
              </tbody>
              <tfoot>
                <tr className="bg-base-200/80 font-bold">
                  <td colSpan={7}>
                    {t("pages.projectDetails.sizingResults.aggregatedTotal")}
                  </td>
                  <td className="text-right tabular-nums">
                    <span className="badge badge-warning rounded-[2px]">
                      {project.totalMachinesResult}{" "}
                      {t("pages.projectDetails.sizingResults.machines")}
                    </span>
                  </td>
                  <td></td>
                </tr>
              </tfoot>
            </table>
          </div>
        </div>
      </section>

      <div className="flex justify-end">
        <button
          type="button"
          className="btn btn-error"
          onClick={() => setDeleteOpen(true)}
        >
          <span className="icon-[fluent--delete-20-regular] size-4" />
          {t("pages.projectDetails.actions.delete")}
        </button>
      </div>

      <ProjectBasicInfoDialog
        open={basicInfoOpen}
        projectId={projectId}
        defaultValues={{
          name: project.name,
          description: project.description,
        }}
        isPending={updateInfoMutation.isPending}
        onClose={() => setBasicInfoOpen(false)}
        onSave={async (payload) => {
          try {
            await updateInfoMutation.mutateAsync({
              id: projectId,
              request: payload,
            });
            setBasicInfoOpen(false);
            showSuccessToast(t("toast.project.updated"));
          } catch (error) {
            const errorCode =
              error instanceof Error ? error.message : undefined;
            showErrorToast(
              t(
                getProjectErrorTranslationKey(
                  errorCode,
                  "toast.project.saveFailed",
                ),
              ),
            );
          }
        }}
      />

      <ProjectComponentSelectionDialog
        open={selectionOpen}
        projectId={projectId}
        selectedCatalogComponentIds={project.selectedCatalogComponents.map(
          (component) => component.id,
        )}
        catalogComponents={catalogComponentsQuery.data ?? []}
        isPending={updateSelectionMutation.isPending}
        onClose={() => setSelectionOpen(false)}
        onSave={async (selectedCatalogComponentIds) => {
          try {
            await updateSelectionMutation.mutateAsync({
              id: projectId,
              request: { selectedCatalogComponentIds },
            });
            setSelectionOpen(false);
            showSuccessToast(t("toast.project.updated"));
          } catch (error) {
            const errorCode =
              error instanceof Error ? error.message : undefined;
            showErrorToast(
              t(
                getProjectErrorTranslationKey(
                  errorCode,
                  "toast.project.saveFailed",
                ),
              ),
            );
          }
        }}
      />

      <ProjectAssumptionsDialog
        open={assumptionsOpen}
        projectId={projectId}
        defaultValues={project.projectAssumption}
        isPending={updateAssumptionsMutation.isPending}
        onClose={() => setAssumptionsOpen(false)}
        onSave={async (payload) => {
          try {
            await updateAssumptionsMutation.mutateAsync({
              id: projectId,
              request: payload,
            });
            setAssumptionsOpen(false);
            showSuccessToast(t("toast.project.updated"));
          } catch (error) {
            const errorCode =
              error instanceof Error ? error.message : undefined;
            showErrorToast(
              t(
                getProjectErrorTranslationKey(
                  errorCode,
                  "toast.project.saveFailed",
                ),
              ),
            );
          }
        }}
      />

      <ProjectDeleteDialog
        open={deleteOpen}
        projectName={project.name}
        isPending={deleteProjectMutation.isPending}
        onClose={() => setDeleteOpen(false)}
        onConfirm={async () => {
          try {
            await deleteProjectMutation.mutateAsync(projectId);
            showSuccessToast(t("toast.project.deleted"));
            router.push("/dashboard/projects");
          } catch (error) {
            const errorCode =
              error instanceof Error ? error.message : undefined;
            showErrorToast(
              t(
                getProjectErrorTranslationKey(
                  errorCode,
                  "toast.project.deleteFailed",
                ),
              ),
            );
          }
        }}
      />
    </div>
  );
}
