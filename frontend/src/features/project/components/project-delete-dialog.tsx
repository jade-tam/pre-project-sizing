"use client";

import { useTranslations } from "next-intl";
import { useEffect, useRef } from "react";

type ProjectDeleteDialogProps = {
  open: boolean;
  projectName: string;
  isPending: boolean;
  onClose: () => void;
  onConfirm: () => Promise<void>;
};

export function ProjectDeleteDialog({
  open,
  projectName,
  isPending,
  onClose,
  onConfirm,
}: ProjectDeleteDialogProps) {
  const t = useTranslations();
  const dialogRef = useRef<HTMLDialogElement>(null);

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

  return (
    <dialog ref={dialogRef} className="modal">
      <div className="modal-box">
        <h3 className="text-lg font-bold">
          {t("pages.projectDetails.deleteDialog.title")}
        </h3>
        <p className="py-4 text-sm">
          {t("pages.projectDetails.deleteDialog.description", {
            name: projectName,
          })}
        </p>
        <div className="alert alert-warning rounded-field">
          <span className="icon-[fluent--warning-20-regular] size-5" />
          <span className="text-sm">
            {t("pages.projectDetails.deleteDialog.warning")}
          </span>
        </div>

        <div className="modal-action">
          <button
            type="button"
            className="btn"
            disabled={isPending}
            onClick={onClose}
          >
            {t("pages.projectDetails.deleteDialog.actions.cancel")}
          </button>
          <button
            type="button"
            className="btn btn-error"
            disabled={isPending}
            onClick={onConfirm}
          >
            {isPending
              ? t("pages.projectDetails.deleteDialog.actions.deleting")
              : t("pages.projectDetails.deleteDialog.actions.delete")}
          </button>
        </div>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button
          type="button"
          onClick={onClose}
          aria-label={t("pages.projectDetails.deleteDialog.actions.close")}
        >
          close
        </button>
      </form>
    </dialog>
  );
}
