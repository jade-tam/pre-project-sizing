export type CatalogComponentResponse = {
  id: number;
  componentKey: string;
  name: string;
  featureName: string;
  machineSpec: string;
  perMachineCapacity: number;
  capacityUnit: string;
  capacityUnitDescription: string;
  haMinimum: number;
  referenceUrl: string | null;
  updatedAt: string | null;
  updatedBy: string | null;
  createdAt: string;
  createdBy: string | null;
  active: boolean;
};
