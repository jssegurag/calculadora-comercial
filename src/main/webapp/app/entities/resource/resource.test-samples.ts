import dayjs from 'dayjs/esm';

import { IResource, NewResource } from './resource.model';

export const sampleWithRequiredData: IResource = {
  id: 25038,
  salary: 31166.87,
  active: false,
};

export const sampleWithPartialData: IResource = {
  id: 8120,
  salary: 23215.55,
  active: true,
  createdBy: 'hand-holding',
  lastModifiedDate: dayjs('2024-06-23T14:36'),
};

export const sampleWithFullData: IResource = {
  id: 27892,
  salary: 1545.11,
  hourlyRate: 10275.83,
  active: true,
  createdBy: 'aw prohibition peaceful',
  createdDate: dayjs('2024-06-23T18:22'),
  lastModifiedBy: 'until',
  lastModifiedDate: dayjs('2024-06-24T10:35'),
};

export const sampleWithNewData: NewResource = {
  salary: 15858.29,
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
