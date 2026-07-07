import { UserRole } from "@/features/user/types/user-role";

export type NavItem = {
  href: string;
  label: string;
  iconClass?: string;
  requiredRole?: UserRole[];
  subItems?: NavItem[];
};

export type DashboardNavConfig = NavItem[];
