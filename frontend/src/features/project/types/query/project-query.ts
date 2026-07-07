export type ProjectQuery = {
  search?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: "ASC" | "DESC";
  deleted?: boolean;
  catalogComponentIds?: number[];
};
