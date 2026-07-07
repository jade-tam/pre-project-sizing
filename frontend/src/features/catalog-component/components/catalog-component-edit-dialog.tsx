"use client";

import { useEffect, useMemo, useRef } from "react";
import { useForm } from "@tanstack/react-form";
import { useTranslations } from "next-intl";
import { z } from "zod";

import FormField from "@/components/form/form-field";
import { createFormSubmitHandler } from "@/lib/form/create-form-submit-handler";
import { normalizeErrors } from "@/lib/form/normalize-errors";
import { shouldShowFieldErrors } from "@/lib/form/should-show-field-errors";
import { showErrorToast, showSuccessToast } from "@/lib/toast/toast";

import { useUpdateCatalogComponentMutation } from "../mutations/use-update-catalog-component";
import type { CatalogComponentResponse } from "../types/response/catalog-component-response";
import { updateCatalogComponentSchema } from "../schemas/update-catalog-component-schema";

const catalogComponentEditSchema = updateCatalogComponentSchema.omit({
  active: true,
});

type CatalogComponentEditFormValues = z.infer<
  typeof catalogComponentEditSchema
>;

type CatalogComponentEditDialogProps = {
  open: boolean;
  component: CatalogComponentResponse | null;
  onClose: () => void;
};

function toDefaultValues(
  component: CatalogComponentResponse | null,
): CatalogComponentEditFormValues {
  return {
    featureName: component?.featureName ?? "",
    machineSpec: component?.machineSpec ?? "",
    perMachineCapacity: component?.perMachineCapacity ?? 0,
    capacityUnit: component?.capacityUnit ?? "",
    capacityUnitDescription: component?.capacityUnitDescription ?? "",
    haMinimum: component?.haMinimum ?? 0,
    referenceUrl: component?.referenceUrl ?? undefined,
  };
}

export function CatalogComponentEditDialog({
  open,
  component,
  onClose,
}: CatalogComponentEditDialogProps) {
  const t = useTranslations();
  const dialogRef = useRef<HTMLDialogElement>(null);
  const updateMutation = useUpdateCatalogComponentMutation();
  const defaultValues = useMemo(() => toDefaultValues(component), [component]);

  const form = useForm({
    defaultValues,
    validators: {
      onSubmit: catalogComponentEditSchema,
    },
    onSubmit: async ({ value }) => {
      if (!component) {
        return;
      }

      try {
        await updateMutation.mutateAsync({
          id: component.id,
          request: {
            featureName: value.featureName.trim(),
            machineSpec: value.machineSpec.trim(),
            perMachineCapacity: value.perMachineCapacity,
            capacityUnit: value.capacityUnit.trim(),
            capacityUnitDescription: value.capacityUnitDescription.trim(),
            haMinimum: value.haMinimum,
            referenceUrl: value.referenceUrl?.trim() || undefined,
            active: true,
          },
        });
        showSuccessToast(t("toast.catalogComponent.updated"));
        onClose();
      } catch (error) {
        showErrorToast(t("toast.catalogComponent.saveFailed"));
      }
    },
  });

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) {
      return;
    }

    if (open && !dialog.open) {
      form.reset();
      dialog.showModal();
      return;
    }

    if (!open && dialog.open) {
      dialog.close();
      form.reset();
    }
  }, [form, open]);

  useEffect(() => {
    form.reset();
  }, [form, defaultValues]);

  return (
    <dialog ref={dialogRef} className="modal">
      <div className="modal-box max-w-4xl">
        <h3 className="text-lg font-bold">
          {t("pages.catalogComponentsEdit.title")}
        </h3>
        <p className="py-2 text-sm text-base-content/70">
          {t("pages.catalogComponentsEdit.description")}
        </p>

        <form
          className="mt-4 grid gap-4 md:grid-cols-2"
          noValidate
          onSubmit={createFormSubmitHandler(form)}
        >
          <div className="md:col-span-2 flex flex-col gap-1">
            <span className="label-text">
              {t("pages.catalogComponentsEdit.fields.componentKey.label")}
            </span>
            <input
              className="input input-bordered w-full bg-base-200"
              readOnly
              value={component?.componentKey ?? "-"}
            />
          </div>

          <form.Field
            name="featureName"
            validators={{
              onChange: catalogComponentEditSchema.shape.featureName,
            }}
          >
            {(field) => (
              <FormField
                label={t(
                  "pages.catalogComponentsEdit.fields.featureName.label",
                )}
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
                  placeholder={t(
                    "pages.catalogComponentsEdit.fields.featureName.placeholder",
                  )}
                />
              </FormField>
            )}
          </form.Field>

          <form.Field
            name="machineSpec"
            validators={{
              onChange: catalogComponentEditSchema.shape.machineSpec,
            }}
          >
            {(field) => (
              <FormField
                label={t(
                  "pages.catalogComponentsEdit.fields.machineSpec.label",
                )}
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
                  placeholder={t(
                    "pages.catalogComponentsEdit.fields.machineSpec.placeholder",
                  )}
                />
              </FormField>
            )}
          </form.Field>

          <form.Field
            name="perMachineCapacity"
            validators={{
              onChange: catalogComponentEditSchema.shape.perMachineCapacity,
            }}
          >
            {(field) => (
              <FormField
                label={t(
                  "pages.catalogComponentsEdit.fields.perMachineCapacity.label",
                )}
                iconClass="icon-[fluent--number-symbol-24-regular]"
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
                  type="number"
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(event) =>
                    field.handleChange(
                      event.target.value === ""
                        ? 0
                        : Number(event.target.value),
                    )
                  }
                  placeholder={t(
                    "pages.catalogComponentsEdit.fields.perMachineCapacity.placeholder",
                  )}
                />
              </FormField>
            )}
          </form.Field>

          <form.Field
            name="capacityUnit"
            validators={{
              onChange: catalogComponentEditSchema.shape.capacityUnit,
            }}
          >
            {(field) => (
              <FormField
                label={t(
                  "pages.catalogComponentsEdit.fields.capacityUnit.label",
                )}
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
                  placeholder={t(
                    "pages.catalogComponentsEdit.fields.capacityUnit.placeholder",
                  )}
                />
              </FormField>
            )}
          </form.Field>

          <form.Field
            name="capacityUnitDescription"
            validators={{
              onChange:
                catalogComponentEditSchema.shape.capacityUnitDescription,
            }}
          >
            {(field) => (
              <FormField
                label={t(
                  "pages.catalogComponentsEdit.fields.capacityUnitDescription.label",
                )}
                useInputWrapper={false}
                iconClass="icon-[fluent--text-description-24-regular]"
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
                  className="textarea textarea-bordered min-h-28 w-full"
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(event) => field.handleChange(event.target.value)}
                  placeholder={t(
                    "pages.catalogComponentsEdit.fields.capacityUnitDescription.placeholder",
                  )}
                />
              </FormField>
            )}
          </form.Field>

          <form.Field
            name="haMinimum"
            validators={{
              onChange: catalogComponentEditSchema.shape.haMinimum,
            }}
          >
            {(field) => (
              <FormField
                label={t("pages.catalogComponentsEdit.fields.haMinimum.label")}
                iconClass="icon-[fluent--number-symbol-24-regular]"
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
                  type="number"
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(event) =>
                    field.handleChange(
                      event.target.value === ""
                        ? 0
                        : Number(event.target.value),
                    )
                  }
                  placeholder={t(
                    "pages.catalogComponentsEdit.fields.haMinimum.placeholder",
                  )}
                />
              </FormField>
            )}
          </form.Field>

          <form.Field
            name="referenceUrl"
            validators={{
              onChange: catalogComponentEditSchema.shape.referenceUrl,
            }}
          >
            {(field) => (
              <FormField
                label={t(
                  "pages.catalogComponentsEdit.fields.referenceUrl.label",
                )}
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
                  value={field.state.value ?? ""}
                  onBlur={field.handleBlur}
                  onChange={(event) =>
                    field.handleChange(event.target.value || undefined)
                  }
                  placeholder={t(
                    "pages.catalogComponentsEdit.fields.referenceUrl.placeholder",
                  )}
                />
              </FormField>
            )}
          </form.Field>

          <div className="modal-action md:col-span-2">
            <button
              type="button"
              className="btn"
              onClick={onClose}
              disabled={updateMutation.isPending}
            >
              {t("pages.catalogComponentsEdit.actions.cancel")}
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={updateMutation.isPending}
            >
              {updateMutation.isPending
                ? t("pages.catalogComponentsEdit.actions.updating")
                : t("pages.catalogComponentsEdit.actions.update")}
            </button>
          </div>
        </form>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button
          type="button"
          onClick={onClose}
          aria-label={t("pages.catalogComponentsEdit.actions.close")}
        >
          close
        </button>
      </form>
    </dialog>
  );
}
