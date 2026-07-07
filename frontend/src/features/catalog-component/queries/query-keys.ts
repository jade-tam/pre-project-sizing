export const catalogComponentQueryKeys = {
  all: ["catalog-components"] as const,
  list: () => [...catalogComponentQueryKeys.all, "list"] as const,
  detail: (id: number) => [...catalogComponentQueryKeys.all, "detail", id] as const,
};
