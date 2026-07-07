export const userQueryKeys = {
  all: ["users"] as const,

  current: () => [...userQueryKeys.all, "me"] as const,
};
