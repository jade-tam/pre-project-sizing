"use client";

import { useEffect, useMemo, useRef } from "react";
import { useForm } from "@tanstack/react-form";
import { useTranslations } from "next-intl";
import { z } from "zod";

import FormField from "@/components/form/form-field";
import { createFormSubmitHandler } from "@/lib/form/create-form-submit-handler";
import { normalizeErrors } from "@/lib/form/normalize-errors";
import { shouldShowFieldErrors } from "@/lib/form/should-show-field-errors";

const updateProjectAssumptionSchema = z.object({
  concurrentUsers: z.number().int(),
  headroom: z.number(),
  requestsPerUserPerSecond: z.number(),
  apiCallsPerRequest: z.number(),
  dbRatioPerRequest: z.number(),
  searchRatioPerRequest: z.number(),
  cacheRatioPerRequest: z.number(),
  kafkaRatioPerRequest: z.number(),
  logBytesPerRequest: z.number().int(),
  authRatio: z.number(),
});

type ProjectAssumptionFormValues = z.infer<typeof updateProjectAssumptionSchema>;

type ProjectAssumptionsDialogProps = {
  open: boolean;
  projectId: number;
  defaultValues: ProjectAssumptionFormValues | null;
  isPending: boolean;
  onClose: () => void;
  onSave: (payload: ProjectAssumptionFormValues) => Promise<void>;
};

const assumptionFields = [
  "concurrentUsers",
  "headroom",
  "requestsPerUserPerSecond",
  "apiCallsPerRequest",
  "dbRatioPerRequest",
  "searchRatioPerRequest",
  "cacheRatioPerRequest",
  "kafkaRatioPerRequest",
  "logBytesPerRequest",
  "authRatio",
] as const;

function toDefaultValues(defaultValues: ProjectAssumptionFormValues | null): ProjectAssumptionFormValues {
  return defaultValues ?? {
    concurrentUsers: 0,
    headroom: 0,
    requestsPerUserPerSecond: 0,
    apiCallsPerRequest: 0,
    dbRatioPerRequest: 0,
    searchRatioPerRequest: 0,
    cacheRatioPerRequest: 0,
    kafkaRatioPerRequest: 0,
    logBytesPerRequest: 0,
    authRatio: 0,
  };
}

export function ProjectAssumptionsDialog({
  open,
  projectId,
  defaultValues,
  isPending,
  onClose,
  onSave,
}: ProjectAssumptionsDialogProps) {
  const t = useTranslations();
  const dialogRef = useRef<HTMLDialogElement>(null);
  const initialValues = useMemo(() => toDefaultValues(defaultValues), [defaultValues]);

  const form = useForm({
    defaultValues: initialValues,
    validators: {
      onSubmit: updateProjectAssumptionSchema,
    },
    onSubmit: async ({ value }) => {
      await onSave(value);
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
      <div className="modal-box max-w-3xl">
        <h3 className="text-lg font-bold">{t("pages.projectDetailsAssumptions.title")}</h3>
        <p className="py-2 text-sm text-base-content/70">{t("pages.projectDetailsAssumptions.description")}</p>

        <form className="mt-4 grid gap-4 md:grid-cols-2" noValidate onSubmit={createFormSubmitHandler(form)}>
          {assumptionFields.map((fieldName) => (
            <form.Field
              key={fieldName}
              name={fieldName}
              validators={{ onChange: updateProjectAssumptionSchema.shape[fieldName] }}
            >
              {(field) => (
                <FormField
                  label={t(`pages.projectDetailsAssumptions.fields.${fieldName}.label`)}
                  iconClass="icon-[fluent--number-symbol-24-regular]"
                  errors={
                    shouldShowFieldErrors(field.state.meta.isTouched, form.state.submissionAttempts)
                      ? normalizeErrors(field.state.meta.errors, t)
                      : []
                  }
                >
                  <input
                    className="flex-1 min-w-0"
                    type="number"
                    value={field.state.value}
                    onBlur={field.handleBlur}
                    onChange={(event) => {
                      const nextValue = event.target.value === "" ? 0 : Number(event.target.value);
                      field.handleChange(nextValue);
                    }}
                  />
                </FormField>
              )}
            </form.Field>
          ))}

          <div className="modal-action md:col-span-2">
            <button type="button" className="btn" disabled={isPending} onClick={onClose}>
              {t("pages.projectDetailsAssumptions.actions.cancel")}
            </button>
            <button type="submit" className="btn btn-primary" disabled={isPending}>
              {isPending ? t("pages.projectDetailsAssumptions.actions.saving") : t("pages.projectDetailsAssumptions.actions.save")}
            </button>
          </div>
        </form>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button type="button" onClick={onClose} aria-label={t("pages.projectDetailsAssumptions.actions.close")}>close</button>
      </form>
    </dialog>
  );
}
