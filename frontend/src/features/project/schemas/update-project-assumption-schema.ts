import { z } from "zod";

export const updateProjectAssumptionSchema = z.object({
  concurrentUsers: z.number().int(),
  headroom: z.number(),
  requestsPerUserPerSecond: z.number(),
  apiCallsPerRequest: z.number(),
  dbRatioPerRequest: z.number(),
  searchRatioPerRequest: z.number(),
  cacheRatioPerRequest: z.number(),
  kafkaRatioPerRequest: z.number(),
  logBytesPerRequest: z.number().int(),
  authRatio: z.number(),
});
