export interface ApiResponse<T> {
  success: boolean;
  status: number;
  code: string;
  message: string;
  data: T;
  errors: Record<string, string>;
  timestamp: string;
}
