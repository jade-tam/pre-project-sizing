"use client";

type DataTablePaginationProps = {
  page: number;
  size: number;
  totalItems: number;
  sizeOptions?: number[];
  onPageChange: (page: number) => void;
  onSizeChange: (size: number) => void;
  pageStatusLabel: (currentPage: number, totalPages: number) => string;
  sizeLabel: string;
  hasPrevious: boolean;
  previousPageLabel: string;
  hasNext: boolean;
  nextPageLabel: string;
};

export function DataTablePagination({
  page,
  size,
  totalItems,
  sizeOptions = [10, 25, 50],
  onPageChange,
  onSizeChange,
  pageStatusLabel,
  sizeLabel,
  previousPageLabel,
  nextPageLabel,
  hasNext,
  hasPrevious,
}: DataTablePaginationProps) {
  const totalPages = Math.max(1, Math.ceil(totalItems / size));
  const currentPage = Math.min(page, totalPages);

  return (
    <div className="w-full px-4 py-3 bg-base-100 border rounded-box">
      <div className="flex min-w-0 items-center justify-between gap-2">
        <div className="min-w-0 truncate text-xs opacity-70 sm:text-sm">
          {pageStatusLabel(currentPage, totalPages)}
        </div>

        <div className="flex min-w-0 shrink-0 items-center justify-end gap-2">
          <label className="flex items-center gap-2">
            <span className="hidden text-xs text-base-content/70 sm:inline shrink-0">
              {sizeLabel}
            </span>
            <select
              className="select select-bordered select-sm"
              value={String(size)}
              onChange={(event) => onSizeChange(Number(event.target.value))}
              aria-label={sizeLabel}
            >
              {sizeOptions.map((option) => (
                <option key={option} value={option}>
                  {option}
                </option>
              ))}
            </select>
          </label>

          <div className="join">
            <button
              type="button"
              className="btn btn-square btn-sm join-item"
              onClick={() => onPageChange(Math.max(1, page - 1))}
              disabled={!hasPrevious}
              aria-label={previousPageLabel}
            >
              <span
                className="icon-[fluent--chevron-left-24-regular] size-4"
                aria-hidden="true"
              />
            </button>
            <button
              type="button"
              className="btn btn-square btn-sm join-item"
              onClick={() => onPageChange(Math.min(totalPages, page + 1))}
              disabled={!hasNext}
              aria-label={nextPageLabel}
            >
              <span
                className="icon-[fluent--chevron-right-24-regular] size-4"
                aria-hidden="true"
              />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
