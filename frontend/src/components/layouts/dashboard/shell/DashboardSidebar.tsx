"use client";

import { useTranslations } from "next-intl";
import { Link, usePathname } from "@/i18n/navigation";
import UserProfileSection from "./UserProfileSection";
import { NavItem } from "../types";
import { UserRole } from "@/features/user/types/user-role";
import { useCurrentUserQuery } from "@/features/user/queries/use-current-user-query";

type DashboardSidebarProps = {
  navItems: NavItem[];
  onClose: () => void;
  onLogoutClick: () => void;
};

function isActivePath(pathname: string, href: string) {
  if (href === "/dashboard") {
    return pathname === href;
  }

  return pathname === href || pathname.startsWith(`${href}/`);
}

function filterNavItemsByRole(
  navItems: NavItem[],
  userRole: UserRole,
): NavItem[] {
  return navItems
    .filter((item) => {
      if (!item.requiredRole || item.requiredRole.length === 0) {
        return true;
      }

      return item.requiredRole.includes(userRole);
    })
    .map((item) => {
      if (!item.subItems) {
        return item;
      }

      return {
        ...item,
        subItems: filterNavItemsByRole(item.subItems, userRole),
      };
    })
    .filter((item) => {
      if (!item.subItems) {
        return true;
      }

      return item.subItems.length > 0;
    });
}

function NavEntry({
  item,
  pathname,
  onClose,
}: {
  item: NavItem;
  pathname: string;
  onClose: () => void;
}) {
  if (item.subItems && item.subItems.length > 0) {
    return (
      <div>
        <div className="flex gap-2 px-4 py-2 text-sm font-semibold italic">
          {item.iconClass ? (
            <span className={`${item.iconClass} text-2xl`} />
          ) : null}
          <span>{item.label}</span>
        </div>
        <div className="flex">
          <div className="divider divider-horizontal" />
          <div className="flex grow flex-col gap-1 pl-4">
            {item.subItems.map((subItem) => (
              <Link
                key={subItem.href}
                href={subItem.href}
                className={`btn btn-ghost btn-primary justify-start gap-4 ${
                  isActivePath(pathname, subItem.href) ? "btn-active" : ""
                }`}
                onClick={onClose}
              >
                {subItem.iconClass ? (
                  <span className={`${subItem.iconClass} text-2xl`} />
                ) : null}
                <span>{subItem.label}</span>
              </Link>
            ))}
          </div>
        </div>
      </div>
    );
  }

  return (
    <Link
      href={item.href}
      className={`btn justify-start gap-4 ${
        isActivePath(pathname, item.href) ? "btn-primary" : "btn-ghost"
      }`}
      onClick={onClose}
    >
      {item.iconClass ? (
        <span className={`${item.iconClass} text-2xl`} />
      ) : null}
      <span>{item.label}</span>
    </Link>
  );
}

export default function DashboardSidebar({
  navItems,
  onClose,
  onLogoutClick,
}: DashboardSidebarProps) {
  const t = useTranslations();
  const pathname = usePathname();
  const { data: currentUser } = useCurrentUserQuery();

  const userRole = currentUser?.role;

  const filteredNavItems = userRole
    ? filterNavItemsByRole(navItems, userRole)
    : navItems;

  return (
    <aside className="flex h-[100dvh] w-84 flex-col border-r bg-base-100 p-2">
      <div className="py-2 flex flex-col gap-1">
        <p className="ml-2 font-serif text-3xl font-black">
          {t("dashboardShell.brand")}
        </p>
        <p className="ml-2 font-bold font-serif">
          {t("dashboardShell.brandDetail")}
        </p>
      </div>

      <div className="mt-6 flex grow flex-col gap-2">
        {filteredNavItems.map((item) => (
          <NavEntry
            key={item.href}
            item={item}
            pathname={pathname}
            onClose={onClose}
          />
        ))}
      </div>

      <div className="mb-1 flex items-center gap-2">
        <Link href="/" className="btn btn-sm grow" onClick={onClose}>
          <span className="icon-[fluent--home-24-regular]" />
          {t("dashboardShell.nav.backToSite")}
        </Link>
      </div>

      <UserProfileSection onLogout={onLogoutClick} onClose={onClose} />
    </aside>
  );
}
