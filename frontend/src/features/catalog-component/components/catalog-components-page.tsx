"use client";

import { useMemo, useState } from "react";
import { useTranslations } from "next-intl";

import { CatalogComponentEditDialog } from "./catalog-component-edit-dialog";
import { useCatalogComponentsQuery } from "../queries/use-catalog-components-query";
import type { CatalogComponentResponse } from "../types/response/catalog-component-response";

function formatNullableDate(value: string | null) {
  if (!value) {
    return "-";
  }

  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return "-";
  }

  return new Intl.DateTimeFormat(undefined, {
    year: "numeric",
    month: "short",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
}

function formatNullableText(value: string | null | undefined) {
  return value?.trim() ? value : "-";
}

function formatNumber(value: number) {
  return new Intl.NumberFormat(undefined, {
    maximumFractionDigits: 2,
  }).format(value);
}

export function CatalogComponentsPage() {
  const t = useTranslations();
  const catalogComponentsQuery = useCatalogComponentsQuery();
  const [selectedComponent, setSelectedComponent] =
    useState<CatalogComponentResponse | null>(null);

  const rows = useMemo(
    () =>
      [...(catalogComponentsQuery.data ?? [])].sort(
        (left, right) => left.id - right.id,
      ),
    [catalogComponentsQuery.data],
  );

  return (
    <div className="space-y-4">
      <div className="overflow-x-auto rounded-box border border-base-300 bg-base-100">
        <table className="table table-zebra table-sm">
          <thead>
            <tr>
              <th>{t("pages.catalogComponents.columns.id")}</th>
              <th>{t("pages.catalogComponents.columns.componentKey")}</th>
              <th>{t("pages.catalogComponents.columns.name")}</th>
              <th>{t("pages.catalogComponents.columns.featureName")}</th>
              <th>{t("pages.catalogComponents.columns.machineSpec")}</th>
              <th>{t("pages.catalogComponents.columns.perMachineCapacity")}</th>
              <th>{t("pages.catalogComponents.columns.capacityUnit")}</th>
              <th>
                {t("pages.catalogComponents.columns.capacityUnitDescription")}
              </th>
              <th>{t("pages.catalogComponents.columns.haMinimum")}</th>
              <th>{t("pages.catalogComponents.columns.referenceUrl")}</th>
              <th>{t("pages.catalogComponents.columns.updatedAt")}</th>
              <th>{t("pages.catalogComponents.columns.updatedBy")}</th>
              <th>{t("pages.catalogComponents.columns.actions")}</th>
            </tr>
          </thead>
          <tbody>
            {catalogComponentsQuery.isPending ? (
              <tr>
                <td colSpan={13} className="py-10 text-center">
                  <span className="loading loading-spinner loading-md" />
                </td>
              </tr>
            ) : catalogComponentsQuery.isError ? (
              <tr>
                <td colSpan={13} className="py-10 text-center text-error">
                  {t("pages.catalogComponents.error")}
                </td>
              </tr>
            ) : rows.length === 0 ? (
              <tr>
                <td
                  colSpan={13}
                  className="py-10 text-center text-base-content/70"
                >
                  {t("pages.catalogComponents.empty")}
                </td>
              </tr>
            ) : (
              rows.map((component) => (
                <tr key={component.id}>
                  <td>{component.id}</td>
                  <td className="font-medium">{component.componentKey}</td>
                  <td>{component.name}</td>
                  <td>{component.featureName}</td>
                  <td>{component.machineSpec}</td>
                  <td>{formatNumber(component.perMachineCapacity)}</td>
                  <td>{component.capacityUnit}</td>
                  <td>{component.capacityUnitDescription}</td>
                  <td>{component.haMinimum}</td>
                  <td className="max-w-[20rem] wrap-anywhere">
                    <p className="" title={component.referenceUrl ?? undefined}>
                      {formatNullableText(component.referenceUrl)}
                    </p>
                  </td>
                  <td>{formatNullableDate(component.updatedAt)}</td>
                  <td>{formatNullableText(component.updatedBy)}</td>
                  <td>
                    <button
                      type="button"
                      className="btn btn-soft btn-sm "
                      onClick={() => setSelectedComponent(component)}
                    >
                      <span className="shrink-0">
                        {t("pages.catalogComponents.actions.update")}
                      </span>
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <CatalogComponentEditDialog
        open={selectedComponent !== null}
        component={selectedComponent}
        onClose={() => setSelectedComponent(null)}
      />
    </div>
  );
}
