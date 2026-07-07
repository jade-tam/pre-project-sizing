import { z } from "zod";

export const createProjectSchema = z.object({
  name: z
    .string()
    .trim()
    .min(3, "validation.project.name.min")
    .max(80, "validation.project.name.max"),
  description: z
    .string()
    .trim()
    .min(5, "validation.project.description.min")
    .max(200, "validation.project.description.max"),
});
