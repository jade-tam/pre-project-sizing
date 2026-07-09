"use client";

import { useEffect, useMemo, useRef } from "react";
import { useForm } from "@tanstack/react-form";
import { useTranslations } from "next-intl";

import FormField from "@/components/form/form-field";
import { createProjectSchema } from "@/features/project/schemas/create-project-schema";
import { createFormSubmitHandler } from "@/lib/form/create-form-submit-handler";
import { normalizeErrors } from "@/lib/form/normalize-errors";
import { shouldShowFieldErrors } from "@/lib/form/should-show-field-errors";
import type { UpdateProjectInfoRequest } from "@/features/project/types/request/update-project-info-request";

type ProjectBasicInfoDialogProps = {
  open: boolean;
  projectId: number;
  defaultValues: UpdateProjectInfoRequest | null;
  isPending: boolean;
  onClose: () => void;
  onSave: (payload: UpdateProjectInfoRequest) => Promise<void>;
};

function toDefaultValues(
  defaultValues: UpdateProjectInfoRequest | null,
): UpdateProjectInfoRequest {
  return defaultValues ?? {
    name: "",
    description: "",
  };
}

export function ProjectBasicInfoDialog({
  open,
  projectId,
  defaultValues,
  isPending,
  onClose,
  onSave,
}: ProjectBasicInfoDialogProps) {
  const t = useTranslations();
  const dialogRef = useRef<HTMLDialogElement>(null);
  const initialValues = useMemo(() => toDefaultValues(defaultValues), [defaultValues]);

  const form = useForm({
    defaultValues: initialValues,
    validators: {
      onSubmit: createProjectSchema,
    },
    onSubmit: async ({ value }) => {
      await onSave({
        name: value.name.trim(),
        description: value.description.trim(),
      });
    },
  });

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
      form.reset();
    }
  }, [form, open]);

  return (
    <dialog ref={dialogRef} className="modal" key={projectId}>
      <div className="modal-box max-w-xl">
        <h3 className="text-lg font-bold">
          {t("pages.projectDetailsBasicInfo.title")}
        </h3>
        <p className="py-2 text-sm text-base-content/70">
          {t("pages.projectDetailsBasicInfo.description")}
        </p>

        <form
          className="mt-4 space-y-4"
          noValidate
          onSubmit={createFormSubmitHandler(form)}
        >
          <form.Field
            name="name"
            validators={{ onChange: createProjectSchema.shape.name }}
          >
            {(field) => (
              <FormField
                label={t("pages.projectDetailsBasicInfo.fields.name.label")}
                errors={
                  shouldShowFieldErrors(
                    field.state.meta.isTouched,
                    form.state.submissionAttempts,
                  )
                    ? normalizeErrors(field.state.meta.errors, t)
                    : []
                }
              >
                <input
                  className="flex-1 min-w-0"
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(event) => field.handleChange(event.target.value)}
                  placeholder={t("pages.projectDetailsBasicInfo.fields.name.placeholder")}
                />
              </FormField>
            )}
          </form.Field>

          <form.Field
            name="description"
            validators={{ onChange: createProjectSchema.shape.description }}
          >
            {(field) => (
              <FormField
                label={t("pages.projectDetailsBasicInfo.fields.description.label")}
                iconClass="icon-[fluent--text-description-24-regular]"
                useInputWrapper={false}
                errors={
                  shouldShowFieldErrors(
                    field.state.meta.isTouched,
                    form.state.submissionAttempts,
                  )
                    ? normalizeErrors(field.state.meta.errors, t)
                    : []
                }
              >
                <textarea
                  className="textarea textarea-bordered min-h-32 w-full"
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(event) => field.handleChange(event.target.value)}
                  placeholder={t("pages.projectDetailsBasicInfo.fields.description.placeholder")}
                />
              </FormField>
            )}
          </form.Field>

          <div className="modal-action">
            <button type="button" className="btn" disabled={isPending} onClick={onClose}>
              {t("pages.projectDetailsBasicInfo.actions.cancel")}
            </button>
            <button type="submit" className="btn btn-primary" disabled={isPending}>
              {isPending
                ? t("pages.projectDetailsBasicInfo.actions.saving")
                : t("pages.projectDetailsBasicInfo.actions.save")}
            </button>
          </div>
        </form>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button
          type="button"
          onClick={onClose}
          aria-label={t("pages.projectDetailsBasicInfo.actions.close")}
        >
          close
        </button>
      </form>
    </dialog>
  );
}
