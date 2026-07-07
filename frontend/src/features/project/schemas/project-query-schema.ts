import { z } from "zod";

export const projectQuerySchema = z.object({
  search: z.string().trim().max(100).optional(),
  page: z.number().int().min(1).optional(),
  size: z.number().int().min(1).max(100).optional(),
  sortBy: z.string().trim().optional(),
  sortDirection: z.enum(["ASC", "DESC"]).optional(),
  deleted: z.boolean().optional(),
  catalogComponentIds: z.array(z.number().int()).optional(),
});
