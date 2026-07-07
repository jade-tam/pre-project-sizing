import type { DashboardNavConfig } from "../../../../features/dashboard/types";

import { getStaffNavConfig } from "./admin-nav";

export function getManagerNavConfig(t: (key: string) => string): DashboardNavConfig {
  return getStaffNavConfig(t);
}
