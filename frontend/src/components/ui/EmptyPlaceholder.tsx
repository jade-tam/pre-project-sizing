export function EmptyPlaceholder({
  label,
  iconClass,
}: {
  label: string;
  iconClass?: string;
}) {
  return (
    <div className="flex flex-col w-full h-full items-center justify-center min-h-[180px]">
      <div className="flex flex-col gap-1 items-center text-base-content/50">
        <span
          className={`${iconClass ? iconClass : "icon-[fluent--box-24-regular] text-2xl"}`}
        ></span>
        <p className="italic">{label}</p>
      </div>
    </div>
  );
}
