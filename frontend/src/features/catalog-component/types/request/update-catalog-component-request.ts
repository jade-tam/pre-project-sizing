export type UpdateCatalogComponentRequest = {
  featureName: string;
  machineSpec: string;
  perMachineCapacity: number;
  capacityUnit: string;
  capacityUnitDescription: string;
  haMinimum: number;
  referenceUrl?: string;
  active?: boolean;
};
