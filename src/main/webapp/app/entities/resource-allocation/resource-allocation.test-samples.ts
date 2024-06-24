import dayjs from 'dayjs/esm';

import { IResourceAllocation, NewResourceAllocation } from './resource-allocation.model';

export const sampleWithRequiredData: IResourceAllocation = {
  id: 18059,
  assignedHours: 31617.08,
};

export const sampleWithPartialData: IResourceAllocation = {
  id: 16063,
  assignedHours: 20588.53,
};

export const sampleWithFullData: IResourceAllocation = {
  id: 32453,
  assignedHours: 16374.16,
  totalCost: 2876.17,
  units: 5013.63,
  capacity: 4858.7,
  plannedHours: 18094.97,
  createdBy: 'complicated ugh deal',
  createdDate: dayjs('2024-06-23T22:17'),
  lastModifiedBy: 'posh with notwithstanding',
  lastModifiedDate: dayjs('2024-06-23T17:27'),
};

export const sampleWithNewData: NewResourceAllocation = {
  assignedHours: 30243.81,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
