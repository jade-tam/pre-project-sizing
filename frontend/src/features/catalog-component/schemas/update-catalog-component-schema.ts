import { z } from "zod";

export const updateCatalogComponentSchema = z.object({
  featureName: z.string().trim().min(3).max(100),
  machineSpec: z.string().trim().min(3).max(100),
  perMachineCapacity: z.number(),
  capacityUnit: z.string().trim().min(1).max(50),
  capacityUnitDescription: z.string().trim().min(5).max(200),
  haMinimum: z.number().int(),
  referenceUrl: z.string().trim().url().max(240).optional(),
  active: z.boolean().optional(),
});
