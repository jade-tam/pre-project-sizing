"use client";

import { DashboardPageShell } from "@/components/layouts/dashboard/dashboard-page-shell";
import ProfileEditorForm from "@/features/auth/components/profile/ProfileEditorForm";
import UserProfilePreviewCard, {
  UserProfilePreviewCardProps,
} from "@/features/auth/components/profile/UserProfilePreviewCard";
import { useCurrentUserQuery } from "@/features/user/queries/use-current-user-query";
import { useTranslations } from "next-intl";
import { useCallback, useState } from "react";

export default function DashboardProfilePage() {
  const t = useTranslations();
  const { data: currentUser } = useCurrentUserQuery();

  const profile = currentUser;
  const [previewState, setPreviewState] =
    useState<UserProfilePreviewCardProps | null>(null);

  const handlePreviewChange = useCallback(
    (nextPreview: UserProfilePreviewCardProps) => {
      setPreviewState(nextPreview);
    },
    [],
  );

  if (!profile || !currentUser) {
    return null;
  }

  const displayPreview = previewState ?? currentUser;

  return (
    <DashboardPageShell
      title={t("pages.profile.title")}
      description={t("profile.description")}
    >
      <main className="rounded-box w-full border border-base-300 bg-base-100 p-6">
        <ProfileEditorForm
          initialProfile={profile}
          onPreviewChange={handlePreviewChange}
          previewCard={
            <UserProfilePreviewCard
              fullName={displayPreview.fullName}
              displayName={
                displayPreview.displayName ?? displayPreview.fullName
              }
              username={displayPreview.username}
              pronouns={displayPreview.pronouns}
              bio={displayPreview.bio}
              avatarUrl={displayPreview.avatarUrl}
            />
          }
        />
      </main>
    </DashboardPageShell>
  );
}
