import { CatalogComponentResponse } from "@/features/catalog-component/types/response/catalog-component-response";
import type { ComponentSizingResultResponse } from "./component-sizing-result-response";
import type { ProjectAssumptionResponse } from "./project-assumption-response";
import type { ProjectOwnerResponse } from "./project-owner-response";

export type ProjectResponse = {
  id: number;
  owner: ProjectOwnerResponse;
  name: string;
  description: string;
  selectedCatalogComponents: CatalogComponentResponse[];
  projectAssumption: ProjectAssumptionResponse | null;
  sizingResults: ComponentSizingResultResponse[];
  totalMachinesResult: number;
  createdAt: string;
  updatedAt: string;
  createdBy: string | null;
  updatedBy: string | null;
};
