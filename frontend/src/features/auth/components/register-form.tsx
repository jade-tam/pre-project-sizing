"use client";

import { useForm } from "@tanstack/react-form";
import { useTranslations } from "next-intl";

import FormField from "@/components/form/form-field";
import PasswordField from "@/features/auth/components/password-field";
import {
  confirmPasswordSchema,
  fullNameSchema,
  normalizedEmailSchema,
  passwordSchema,
  registerSchema,
  usernameSchema,
} from "@/features/auth/schemas/register-schema";
import { Link } from "@/i18n/navigation";
import { createFormSubmitHandler } from "@/lib/form/create-form-submit-handler";
import { normalizeErrors } from "@/lib/form/normalize-errors";
import { useRegister } from "../mutations/use-register";

export default function RegisterForm() {
  const t = useTranslations();

  const registerMutation = useRegister();

  const form = useForm({
    defaultValues: {
      fullName: "",
      username: "",
      email: "",
      password: "",
      confirmPassword: "",
    },
    validators: {
      onSubmit: registerSchema,
    },
    onSubmit: ({ value }) => {
      const { confirmPassword, ...registerData } = value;
      registerMutation.mutate(registerData);
    },
  });

  return (
    <form
      className="card bg-base-100 card-border border-base-300 card-sm overflow-hidden"
      noValidate
      onSubmit={createFormSubmitHandler(form)}
    >
      <div className="border-base-300 border-b border-dashed">
        <div className="flex items-center gap-2 p-4 text-sm font-medium">
          <span
            className="icon-[fluent--person-add-24-regular] size-5 opacity-40"
            aria-hidden="true"
          />
          {t("auth.register.title")}
        </div>
      </div>

      <div className="card-body gap-4">
        <p className="text-xs opacity-60">{t("auth.register.description")}</p>

        <form.Field
          name="fullName"
          validators={{
            onChange: fullNameSchema,
          }}
        >
          {(field) => (
            <FormField
              label={t("auth.fields.fullName.label")}
              iconClass="icon-[fluent--person-24-regular]"
              errors={
                field.state.meta.isTouched
                  ? normalizeErrors(field.state.meta.errors, t)
                  : []
              }
              showMoreLabel={(count) =>
                t("auth.validation.showMoreErrors", { count })
              }
              showLessLabel={t("auth.validation.showLessErrors")}
            >
              <input
                className="flex-1 min-w-0"
                value={field.state.value}
                onBlur={field.handleBlur}
                onChange={(event) => field.handleChange(event.target.value)}
                placeholder={t("auth.fields.fullName.placeholder")}
              />
            </FormField>
          )}
        </form.Field>

        <form.Field
          name="username"
          validators={{
            onChange: usernameSchema,
          }}
        >
          {(field) => (
            <FormField
              label={t("auth.fields.username.label")}
              iconClass="icon-[fluent--person-tag-24-regular]"
              errors={
                field.state.meta.isTouched
                  ? normalizeErrors(field.state.meta.errors, t)
                  : []
              }
              showMoreLabel={(count) =>
                t("auth.validation.showMoreErrors", { count })
              }
              showLessLabel={t("auth.validation.showLessErrors")}
            >
              <input
                className="flex-1 min-w-0"
                value={field.state.value}
                onBlur={field.handleBlur}
                onChange={(event) => field.handleChange(event.target.value)}
                placeholder={t("auth.fields.username.placeholder")}
              />
            </FormField>
          )}
        </form.Field>

        <form.Field
          name="email"
          validators={{
            onChange: normalizedEmailSchema,
          }}
        >
          {(field) => (
            <FormField
              label={t("auth.fields.email.label")}
              iconClass="icon-[fluent--mail-24-regular]"
              errors={
                field.state.meta.isTouched
                  ? normalizeErrors(field.state.meta.errors, t)
                  : []
              }
              showMoreLabel={(count) =>
                t("auth.validation.showMoreErrors", { count })
              }
              showLessLabel={t("auth.validation.showLessErrors")}
            >
              <input
                className="flex-1 min-w-0"
                type="email"
                value={field.state.value}
                onBlur={field.handleBlur}
                onChange={(event) => field.handleChange(event.target.value)}
                placeholder={t("auth.fields.email.placeholder")}
              />
            </FormField>
          )}
        </form.Field>

        <form.Field
          name="password"
          validators={{
            onChange: passwordSchema,
          }}
        >
          {(field) => (
            <PasswordField
              label={t("auth.fields.password.label")}
              value={field.state.value}
              errors={
                field.state.meta.isTouched
                  ? normalizeErrors(field.state.meta.errors, t)
                  : []
              }
              onChange={field.handleChange}
              onBlur={field.handleBlur}
              placeholder={t("auth.fields.password.placeholder")}
              showStrength={true}
              showLabel={t("auth.fields.password.show")}
              hideLabel={t("auth.fields.password.hide")}
              showMoreErrorsLabel={(count) =>
                t("auth.validation.showMoreErrors", { count })
              }
              showLessErrorsLabel={t("auth.validation.showLessErrors")}
              strengthLabels={{
                weak: t("auth.validation.passwordStrength.weak"),
                fair: t("auth.validation.passwordStrength.fair"),
                good: t("auth.validation.passwordStrength.good"),
                strong: t("auth.validation.passwordStrength.strong"),
              }}
            />
          )}
        </form.Field>

        <form.Field
          name="confirmPassword"
          validators={{
            onChange: confirmPasswordSchema,
          }}
        >
          {(field) => (
            <PasswordField
              label={t("auth.fields.confirmPassword.label")}
              value={field.state.value}
              errors={
                field.state.meta.isTouched
                  ? normalizeErrors(field.state.meta.errors, t)
                  : []
              }
              onChange={field.handleChange}
              onBlur={field.handleBlur}
              placeholder={t("auth.fields.confirmPassword.placeholder")}
              showStrength={false}
              showLabel={t("auth.fields.password.show")}
              hideLabel={t("auth.fields.password.hide")}
              showMoreErrorsLabel={(count) =>
                t("auth.validation.showMoreErrors", { count })
              }
              showLessErrorsLabel={t("auth.validation.showLessErrors")}
              strengthLabels={{
                weak: t("auth.validation.passwordStrength.weak"),
                fair: t("auth.validation.passwordStrength.fair"),
                good: t("auth.validation.passwordStrength.good"),
                strong: t("auth.validation.passwordStrength.strong"),
              }}
            />
          )}
        </form.Field>

        <button
          className="btn btn-primary w-full"
          type="submit"
          disabled={registerMutation.isPending}
        >
          {registerMutation.isPending
            ? t("auth.register.submitting")
            : t("auth.register.submit")}
        </button>

        <Link href="/login" className="btn btn-ghost btn-sm w-full">
          {t("auth.actions.orLogin")}
        </Link>
      </div>
    </form>
  );
}
