import { UserRole } from "./user-role";

export interface CurrentUser {
  id: string;
  username: string;
  email: string;
  fullName: string;
  role: UserRole;
  avatarUrl: string | null;
  displayName: string | null;
  pronouns: string | null;
  bio: string | null;
}
