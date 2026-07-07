import { clientEnvSchema } from "./schema";

export const clientEnv = clientEnvSchema.parse({
  NEXT_PUBLIC_BASE_URL: process.env.NEXT_PUBLIC_BASE_URL,
  NEXT_PUBLIC_REST_API_BASE_URL: process.env.NEXT_PUBLIC_REST_API_BASE_URL,
});
