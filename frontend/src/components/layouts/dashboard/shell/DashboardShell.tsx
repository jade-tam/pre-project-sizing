"use client";

import { ConfirmationModal } from "@/components/confirmation-modal";
import { useLogout } from "@/features/auth/mutations/use-logout";
import { useTranslations } from "next-intl";
import { useState } from "react";
import { NavItem } from "../types";
import DashboardSidebar from "./DashboardSidebar";
import DashboardTopbar from "./DashboardTopbar";

type DashboardShellProps = {
  children: React.ReactNode;
  navItems: NavItem[];
};

export default function DashboardShell({
  children,
  navItems,
}: DashboardShellProps) {
  const t = useTranslations();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const logoutMutation = useLogout();

  const handleLogout = () => {
    logoutMutation.mutate();
    setShowLogoutModal(false);
  };

  return (
    <div className="drawer h-[100dvh] overflow-hidden lg:drawer-open motion-safe:dashboard-shell-enter">
      <input
        id="dashboard-drawer"
        type="checkbox"
        className="drawer-toggle"
        checked={sidebarOpen}
        readOnly
      />
      <div className="drawer-content flex h-[100dvh] min-h-0 flex-col overflow-hidden">
        <DashboardTopbar
          onMenuClick={() => setSidebarOpen((value) => !value)}
        />

        <div className="w-full flex-1 min-h-0 overflow-y-auto p-2">
          {children}
        </div>
      </div>

      <div className="drawer-side z-50">
        <label
          htmlFor="dashboard-drawer"
          className="drawer-overlay"
          onClick={() => setSidebarOpen(false)}
          aria-label={t("dashboardShell.actions.closeDrawer")}
        />
        <DashboardSidebar
          navItems={navItems}
          onClose={() => setSidebarOpen(false)}
          onLogoutClick={() => setShowLogoutModal(true)}
        />
      </div>

      <ConfirmationModal
        isOpen={showLogoutModal}
        title={t("dashboardShell.logoutModal.title")}
        message={t("dashboardShell.logoutModal.message")}
        confirmLabel={
          logoutMutation.isPending
            ? t("dashboardShell.actions.signingOut")
            : t("dashboardShell.logoutModal.confirm")
        }
        cancelLabel={t("dashboardShell.logoutModal.cancel")}
        closeLabel={t("dashboardShell.logoutModal.close")}
        confirmClassName="btn btn-error"
        isConfirming={logoutMutation.isPending}
        onConfirm={handleLogout}
        onCancel={() => setShowLogoutModal(false)}
      />
    </div>
  );
}
