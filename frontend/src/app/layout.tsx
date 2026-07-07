import { QueryProvider } from "@/providers/QueryProvider";
import ToastProvider from "@/providers/toast-provider";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <QueryProvider>
      {children}
      <ToastProvider />
    </QueryProvider>
  );
}
