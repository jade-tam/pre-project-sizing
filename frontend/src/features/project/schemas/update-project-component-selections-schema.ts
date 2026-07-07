import { z } from "zod";

export const updateProjectComponentSelectionsSchema = z.object({
  selectedCatalogComponentIds: z.array(z.number().int()),
});
