import type { ProjectQuery } from "../types/query/project-query";

function parsePositiveInteger(value: string | null): number | undefined {
  if (!value) {
    return undefined;
  }

  const parsed = Number.parseInt(value, 10);
  return Number.isFinite(parsed) && parsed > 0 ? parsed : undefined;
}

function parseCatalogComponentIds(
  searchParams: URLSearchParams,
): number[] | undefined {
  const ids = searchParams
    .getAll("catalogComponentIds")
    .map((value) => Number.parseInt(value, 10))
    .filter((value) => Number.isFinite(value) && value > 0);

  return ids.length > 0 ? ids : undefined;
}

export function parseProjectQuery(searchParams: URLSearchParams): ProjectQuery {
  return {
    search: searchParams.get("search")?.trim() || undefined,
    page: parsePositiveInteger(searchParams.get("page")),
    size: parsePositiveInteger(
      searchParams.get("pageSize") ?? searchParams.get("size"),
    ),
    sortBy: searchParams.get("sortBy")?.trim() || undefined,
    sortDirection:
      searchParams.get("sortDirection") === "ASC" ||
      searchParams.get("sortDirection") === "DESC"
        ? searchParams.get("sortDirection")
        : undefined,
    catalogComponentIds: parseCatalogComponentIds(searchParams),
  };
}

export function toProjectSearchParams(query: ProjectQuery): URLSearchParams {
  const params = new URLSearchParams();

  if (query.search) {
    params.set("search", query.search);
  }

  if (query.page) {
    params.set("page", String(query.page));
  }

  if (query.size) {
    params.set("pageSize", String(query.size));
  }

  if (query.sortBy) {
    params.set("sortBy", query.sortBy);
  }

  if (query.sortDirection) {
    params.set("sortDirection", query.sortDirection);
  }

  query.catalogComponentIds?.forEach((id) => {
    params.append("catalogComponentIds", String(id));
  });

  return params;
}
