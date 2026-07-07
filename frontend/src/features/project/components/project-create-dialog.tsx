"use client";

import { useEffect, useRef } from "react";
import { useForm } from "@tanstack/react-form";
import { useTranslations } from "next-intl";

import FormField from "@/components/form/form-field";
import { createProjectSchema } from "@/features/project/schemas/create-project-schema";
import { createFormSubmitHandler } from "@/lib/form/create-form-submit-handler";
import { normalizeErrors } from "@/lib/form/normalize-errors";
import { shouldShowFieldErrors } from "@/lib/form/should-show-field-errors";

type ProjectCreateDialogProps = {
  open: boolean;
  onClose: () => void;
  isPending: boolean;
  onCreated: (payload: { name: string; description: string }) => Promise<void>;
};

const defaultValues = {
  name: "",
  description: "",
};

export function ProjectCreateDialog({ open, onClose, isPending, onCreated }: ProjectCreateDialogProps) {
  const t = useTranslations();
  const dialogRef = useRef<HTMLDialogElement>(null);

  const form = useForm({
    defaultValues,
    validators: {
      onSubmit: createProjectSchema,
    },
    onSubmit: async ({ value }) => {
      await onCreated({
        name: value.name.trim(),
        description: value.description.trim(),
      });
      form.reset();
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
    <dialog ref={dialogRef} className="modal">
      <div className="modal-box max-w-xl">
        <h3 className="text-lg font-bold">{t("pages.projectCreate.title")}</h3>
        <p className="py-2 text-sm text-base-content/70">
          {t("pages.projectCreate.description")}
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
                label={t("pages.projectCreate.fields.name.label")}
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
                  placeholder={t("pages.projectCreate.fields.name.placeholder")}
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
                label={t("pages.projectCreate.fields.description.label")}
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
                  placeholder={t(
                    "pages.projectCreate.fields.description.placeholder",
                  )}
                />
              </FormField>
            )}
          </form.Field>

          <div className="modal-action">
            <button type="button" className="btn" onClick={onClose}>
              {t("pages.projectCreate.actions.cancel")}
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={isPending}
            >
              {isPending
                ? t("pages.projectCreate.actions.creating")
                : t("pages.projectCreate.actions.create")}
            </button>
          </div>
        </form>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button
          type="button"
          onClick={onClose}
          aria-label={t("pages.projectCreate.actions.close")}
        >
          close
        </button>
      </form>
    </dialog>
  );
}
