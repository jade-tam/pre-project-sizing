"use client";

import { useTranslations } from "next-intl";
import { useEffect, useMemo, useRef, useState } from "react";

import type { CatalogComponentResponse } from "@/features/catalog-component/types/response/catalog-component-response";

type ProjectComponentSelectionDialogProps = {
  open: boolean;
  projectId: number;
  selectedCatalogComponentIds: number[];
  catalogComponents: CatalogComponentResponse[];
  isPending: boolean;
  onClose: () => void;
  onSave: (selectedCatalogComponentIds: number[]) => Promise<void>;
};

export function ProjectComponentSelectionDialog({
  open,
  projectId,
  selectedCatalogComponentIds,
  catalogComponents,
  isPending,
  onClose,
  onSave,
}: ProjectComponentSelectionDialogProps) {
  const t = useTranslations();
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [localSelectedIds, setLocalSelectedIds] = useState<number[]>(
    selectedCatalogComponentIds,
  );

  useEffect(() => {
    if (open) {
      setLocalSelectedIds(selectedCatalogComponentIds);
    }
  }, [open, selectedCatalogComponentIds]);

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) {
      return;
    }

    if (open && !dialog.open) {
      dialog.showModal();
      return;
    }

    if (!open && dialog.open) {
      dialog.close();
    }
  }, [open]);

  const selectedSet = useMemo(
    () => new Set(localSelectedIds),
    [localSelectedIds],
  );

  const apiAppCompId = useMemo(
    () => catalogComponents.find((comp) => comp.componentKey === "API_APP")!.id,
    [catalogComponents],
  );

  const k8sMasterCompId = useMemo(
    () =>
      catalogComponents.find((comp) => comp.componentKey === "K8S_MASTER")!.id,
    [catalogComponents],
  );

  return (
    <dialog ref={dialogRef} className="modal" key={projectId}>
      <div className="modal-box max-w-3xl">
        <h3 className="text-lg font-bold">
          {t("pages.projectDetailsSelection.title")}
        </h3>
        <p className="py-2 text-sm text-base-content/70">
          {t("pages.projectDetailsSelection.description")}
        </p>

        <div className="mt-4 max-h-[50vh] overflow-auto">
          <div className="grid gap-2 md:grid-cols-2">
            {catalogComponents
              .sort((a, b) => a.id - b.id)
              .map((component) => {
                const checked = selectedSet.has(component.id);
                const disabled =
                  component.id === k8sMasterCompId &&
                  !selectedSet.has(apiAppCompId);

                return (
                  <label
                    key={component.id}
                    className="flex cursor-pointer items-center gap-3 rounded-box border border-base-300 p-3"
                  >
                    <input
                      type="checkbox"
                      className="checkbox checkbox-primary"
                      disabled={disabled}
                      checked={checked}
                      onChange={(event) => {
                        const nextChecked = event.target.checked;
                        setLocalSelectedIds((previous) => {
                          if (nextChecked) {
                            return previous.includes(component.id)
                              ? previous
                              : [...previous, component.id];
                          }

                          console.log(nextChecked, component.id, previous);

                          if (component.id === apiAppCompId) {
                            previous = previous.filter(
                              (id) => id !== k8sMasterCompId,
                            );
                          }

                          return previous.filter((id) => id !== component.id);
                        });
                      }}
                    />
                    <span className="flex-1">
                      <span className="block font-medium">
                        {component.name}
                      </span>
                      <span className="block text-xs text-base-content/60">
                        {component.featureName}
                      </span>
                    </span>
                  </label>
                );
              })}
          </div>
        </div>

        <div className="modal-action">
          <button
            type="button"
            className="btn"
            disabled={isPending}
            onClick={onClose}
          >
            {t("pages.projectDetailsSelection.actions.cancel")}
          </button>
          <button
            type="button"
            className="btn btn-primary"
            disabled={isPending}
            onClick={async () => {
              await onSave(localSelectedIds);
            }}
          >
            {isPending
              ? t("pages.projectDetailsSelection.actions.saving")
              : t("pages.projectDetailsSelection.actions.save")}
          </button>
        </div>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button
          type="button"
          onClick={onClose}
          aria-label={t("pages.projectDetailsSelection.actions.close")}
        >
          close
        </button>
      </form>
    </dialog>
  );
}
