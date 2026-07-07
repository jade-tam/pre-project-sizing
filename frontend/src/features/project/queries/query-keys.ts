export const projectQueryKeys = {
  all: ["projects"] as const,
  list: (query: unknown) => [...projectQueryKeys.all, "list", query] as const,
  detail: (id: number) => [...projectQueryKeys.all, "detail", id] as const,
  catalogComponents: () => [...projectQueryKeys.all, "catalog-components"] as const,
  catalogComponent: (id: number) => [...projectQueryKeys.all, "catalog-component", id] as const,
};
