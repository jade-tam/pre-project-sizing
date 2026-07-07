"use client";

import type { ReactNode, RefObject } from "react";

import type {
  TableFilterConfig,
  TableFilterValue,
} from "@/components/data-table/types";

type DataTableToolbarProps = {
  query: string;
  onQueryChange: (value: string) => void;
  searchPlaceholder?: string;
  searchInputRef?: RefObject<HTMLInputElement | null>;
  filters?: TableFilterConfig[];
  filterValues?: Record<string, TableFilterValue>;
  onFilterChange?: (id: string, value: TableFilterValue) => void;
  searchLabel?: string;
  searchAriaLabel?: string;
  endContent?: ReactNode;
};

export function DataTableToolbar({
  query,
  onQueryChange,
  searchPlaceholder,
  searchInputRef,
  filters = [],
  filterValues = {},
  onFilterChange,
  searchLabel,
  searchAriaLabel,
  endContent,
}: DataTableToolbarProps) {
  return (
    <div className="flex flex-col gap-3 md:items-center md:justify-between">
      <div className="flex w-full items-center gap-2">
        <span className="icon-[fluent--filter-24-regular] text-xl shrink-0"></span>
        {filters.map((filter) =>
          renderFilter(filterValues, filter, onFilterChange),
        )}
      </div>

      <div className="flex w-full flex-col gap-2 md:flex-row md:items-center md:justify-between">
        <label className="input input-bordered flex w-full items-center gap-2 md:max-w-sm">
          <span
            className="icon-[fluent--search-24-regular] size-4 opacity-60"
            aria-hidden="true"
          />
          <span className="sr-only">{searchLabel}</span>
          <input
            ref={searchInputRef}
            type="search"
            role="searchbox"
            value={query}
            onChange={(event) => onQueryChange(event.target.value)}
            placeholder={searchPlaceholder}
            className="grow"
            aria-label={searchAriaLabel ?? searchLabel}
          />
          <kbd className="kbd kbd-sm">Ctrl</kbd>
          <kbd className="kbd kbd-sm">K</kbd>
        </label>

        <div className="flex w-full items-center justify-between gap-2 md:w-auto md:justify-end">
          {endContent}
        </div>
      </div>
    </div>
  );
}

// Invidual filter rendering logic is separated to keep the main component clean and readable
function renderFilter(
  filterValues: Record<string, TableFilterValue>,
  filter: TableFilterConfig,
  onFilterChange: ((id: string, value: TableFilterValue) => void) | undefined,
) {
  const activeValue = filterValues[filter.id];

  if (filter.isMulti) {
    const values = Array.isArray(activeValue)
      ? activeValue
      : activeValue
        ? [activeValue]
        : [];

    return (
      <form key={filter.id} className="filter gap-y-1">
        <input
          className="btn btn-square btn-xs btn-error"
          type="reset"
          value="×"
          aria-label={`Clear ${filter.label}`}
          onClick={() => onFilterChange?.(filter.id, [])}
        />
        {filter.options.map((option) => (
          <input
            key={option.value}
            className="btn btn-soft btn-xs checked:btn-neutral"
            type="checkbox"
            name={filter.id}
            aria-label={option.label}
            checked={values.includes(option.value)}
            onChange={(event) => {
              const nextValues = event.target.checked
                ? [...values, option.value]
                : values.filter((v) => v !== option.value);
              onFilterChange?.(filter.id, nextValues);
            }}
          />
        ))}
      </form>
    );
  }

  return (
    <label key={filter.id} className="flex items-center">
      <span className="sr-only">{filter.label}</span>
      <select
        value={typeof activeValue === "string" ? activeValue : ""}
        onChange={(event) => onFilterChange?.(filter.id, event.target.value)}
        aria-label={filter.label}
        className="select select-bordered select-sm"
      >
        <option value="">{filter.clearOptionLabel ?? filter.label}</option>
        {filter.options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
    </label>
  );
}
