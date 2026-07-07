export type PaginationResponse<T> = {
  pageData: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  hasPrevious: boolean;
  hasNext: boolean;
};
