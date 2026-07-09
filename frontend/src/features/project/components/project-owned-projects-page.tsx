"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useLocale, useTranslations } from "next-intl";
import { useSearchParams } from "next/navigation";

import { DataTablePagination } from "@/components/data-table/data-table-pagination";
import { DataTableToolbar } from "@/components/data-table/data-table-toolbar";
import type {
  DataTableSortDirection,
  TableFilterConfig,
  TableFilterValue,
} from "@/components/data-table/types";
import { useCatalogComponentsQuery } from "@/features/catalog-component/queries/use-catalog-components-query";
import { useCreateProjectMutation } from "@/features/project/mutations/use-create-project";
import { useOwnedProjectsQuery } from "@/features/project/queries/use-owned-projects-query";
import {
  parseProjectQuery,
  toProjectSearchParams,
} from "@/features/project/utils/project-query-state";
import { Link, usePathname, useRouter } from "@/i18n/navigation";
import { getProjectErrorTranslationKey } from "@/features/project/errors";
import { showErrorToast, showSuccessToast } from "@/lib/toast/toast";

import { ProjectCreateDialog } from "./project-create-dialog";

function formatDate(value: string, locale: string) {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return "-";
  }

  return new Intl.DateTimeFormat(locale, {
    year: "numeric",
    month: "short",
    day: "2-digit",
  }).format(date);
}

function cycleSortValue(
  currentSortBy: string | undefined,
  currentDirection: DataTableSortDirection | undefined,
  nextSortBy: string,
): { sortBy?: string; sortDirection?: DataTableSortDirection } {
  if (currentSortBy !== nextSortBy) {
    return { sortBy: nextSortBy, sortDirection: "asc" };
  }

  if (currentDirection === "asc") {
    return { sortBy: nextSortBy, sortDirection: "desc" };
  }

  return { sortBy: undefined, sortDirection: undefined };
}

export function ProjectOwnedProjectsPage() {
  const t = useTranslations();
  const locale = useLocale();
  const pathname = usePathname();
  const router = useRouter();
  const searchParams = useSearchParams();
  const query = useMemo(
    () => parseProjectQuery(new URLSearchParams(searchParams.toString())),
    [searchParams],
  );
  const projectsQuery = useOwnedProjectsQuery(query);
  const catalogComponentsQuery = useCatalogComponentsQuery();
  const createMutation = useCreateProjectMutation();
  const [createOpen, setCreateOpen] = useState(false);
  const [searchInputValue, setSearchInputValue] = useState(query.search ?? "");

  useEffect(() => {
    setSearchInputValue(query.search ?? "");
  }, [query.search]);

  const replaceQuery = useCallback(
    (nextQuery: typeof query) => {
      const params = toProjectSearchParams(nextQuery);
      const nextSearch = params.toString();
      router.replace(nextSearch ? `${pathname}?${nextSearch}` : pathname);
    },
    [pathname, router],
  );

  useEffect(() => {
    const timeoutId = window.setTimeout(() => {
      if ((query.search ?? "") !== searchInputValue) {
        replaceQuery({
          ...query,
          page: 1,
          search: searchInputValue.trim() || undefined,
        });
      }
    }, 1000);

    return () => window.clearTimeout(timeoutId);
  }, [query, replaceQuery, searchInputValue]);

  const filterConfig = useMemo<TableFilterConfig[]>(
    () => [
      {
        id: "catalogComponentIds",
        label: t("pages.projectsMe.catalogComponentFilterLabel"),
        clearOptionLabel: t("pages.projectsMe.allCatalogComponentsLabel"),
        isMulti: true,
        options: (catalogComponentsQuery.data ?? []).map((component) => ({
          value: String(component.id),
          label: component.name,
        })),
      },
    ],
    [catalogComponentsQuery.data, t],
  );

  const activeFilters = useMemo<Record<string, TableFilterValue>>(
    () => ({
      catalogComponentIds: (query.catalogComponentIds ?? []).map(String),
    }),
    [query.catalogComponentIds],
  );

  const handleCreate = useCallback(
    async (payload: { name: string; description: string }) => {
      try {
        const created = await createMutation.mutateAsync(payload);
        setCreateOpen(false);
        showSuccessToast(t("toast.project.created"));
        router.push(`/dashboard/projects/${created.data.id}`);
      } catch (error) {
        const errorCode = error instanceof Error ? error.message : undefined;
        showErrorToast(
          t(
            getProjectErrorTranslationKey(
              errorCode,
              "toast.project.createFailed",
            ),
          ),
        );
      }
    },
    [createMutation, router, t],
  );

  const handleFilterChange = useCallback(
    (id: string, value: TableFilterValue) => {
      if (id !== "catalogComponentIds") {
        return;
      }

      const nextIds = Array.isArray(value)
        ? value
            .map((entry) => Number.parseInt(entry, 10))
            .filter((entry) => Number.isFinite(entry) && entry > 0)
        : [];

      replaceQuery({
        ...query,
        page: 1,
        catalogComponentIds: nextIds.length > 0 ? nextIds : undefined,
      });
    },
    [query, replaceQuery],
  );

  if (projectsQuery.isError) {
    return (
      <div className="alert alert-error">
        {t("toast.project.loadListFailed")}
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <DataTableToolbar
        query={searchInputValue}
        onQueryChange={setSearchInputValue}
        searchLabel={t("pages.projectsMe.searchLabel")}
        searchPlaceholder={t("pages.projectsMe.searchPlaceholder")}
        filters={filterConfig}
        filterValues={activeFilters}
        onFilterChange={handleFilterChange}
        endContent={
          <button
            type="button"
            className="btn btn-primary"
            onClick={() => setCreateOpen(true)}
          >
            <span
              className="icon-[fluent--add-24-regular] size-4"
              aria-hidden="true"
            />
            {t("pages.projectsMe.createAction")}
          </button>
        }
      />

      <div className="overflow-x-auto rounded-box border border-base-300 bg-base-100">
        <table className="table">
          <thead>
            <tr>
              <th>{t("pages.projectsMe.columns.name")}</th>
              <th>{t("pages.projectsMe.columns.createdAt")}</th>
              <th>{t("pages.projectsMe.columns.updatedAt")}</th>
              <th>{t("pages.projectsMe.columns.actions")}</th>
            </tr>
          </thead>
          <tbody>
            {projectsQuery.isPending ? (
              <tr>
                <td colSpan={4} className="py-8 text-center">
                  <span className="loading loading-spinner loading-md" />
                </td>
              </tr>
            ) : (projectsQuery.data?.pageData ?? []).length === 0 ? (
              <tr>
                <td
                  colSpan={4}
                  className="py-8 text-center text-base-content/70"
                >
                  {t("pages.projectsMe.empty")}
                </td>
              </tr>
            ) : (
              (projectsQuery.data?.pageData ?? []).map((project) => (
                <tr key={project.id}>
                  <td className="align-top">
                    <Link
                      href={`/dashboard/projects/${project.id}`}
                      className=""
                    >
                      <div className="flex items-start justify-between gap-4 min-w-[300px] max-w-[500px]">
                        <div className="min-w-0 space-y-1">
                          <div className="wrap-break-word text-base font-semibold leading-snug">
                            {project.name}
                          </div>
                          <div className="wrap-break-word text-sm leading-snug text-base-content/70">
                            {project.description}
                          </div>
                          <div className="flex flex-wrap gap-1.5 pt-1">
                            {project.selectedCatalogComponents.map(
                              (component) => (
                                <span
                                  key={component.id}
                                  className="badge badge-xs badge-primary badge-soft"
                                >
                                  {component.name}
                                </span>
                              ),
                            )}
                          </div>
                        </div>
                      </div>
                    </Link>
                  </td>
                  <td>{formatDate(project.createdAt, locale)}</td>
                  <td>{formatDate(project.updatedAt, locale)}</td>
                  <td>
                    <Link
                      href={`/dashboard/projects/${project.id}`}
                      className="btn btn-soft btn-sm"
                    >
                      {t("pages.projectsMe.viewAction")}
                    </Link>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {projectsQuery.data ? (
        <DataTablePagination
          page={projectsQuery.data.page}
          size={projectsQuery.data.size}
          totalItems={projectsQuery.data.totalElements}
          onPageChange={(page) => replaceQuery({ ...query, page })}
          onSizeChange={(size) => replaceQuery({ ...query, page: 1, size })}
          pageStatusLabel={(currentPage, totalPages) =>
            t("table.pageStatus", { currentPage, totalPages })
          }
          sizeLabel={t("table.rowsPerPage")}
          previousPageLabel={t("table.previousPage")}
          nextPageLabel={t("table.nextPage")}
          hasNext={projectsQuery.data.hasNext}
          hasPrevious={projectsQuery.data.hasPrevious}
        />
      ) : null}

      <ProjectCreateDialog
        open={createOpen}
        onClose={() => setCreateOpen(false)}
        isPending={createMutation.isPending}
        onCreated={handleCreate}
      />
    </div>
  );
}
