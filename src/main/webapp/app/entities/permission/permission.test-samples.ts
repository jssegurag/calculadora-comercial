import dayjs from 'dayjs/esm';

import { IPermission, NewPermission } from './permission.model';

export const sampleWithRequiredData: IPermission = {
  id: 18313,
  name: 'irresponsible',
};

export const sampleWithPartialData: IPermission = {
  id: 1246,
  name: 'ha rudely',
  description: 'quickly curdle',
  createdDate: dayjs('2024-06-24T13:12'),
};

export const sampleWithFullData: IPermission = {
  id: 11115,
  name: 'plaintive alongside instead',
  description: 'knottily cutover',
  createdBy: 'brightly',
  createdDate: dayjs('2024-06-23T20:25'),
  lastModifiedBy: 'hair an',
  lastModifiedDate: dayjs('2024-06-24T10:13'),
};

export const sampleWithNewData: NewPermission = {
  name: 'blah hence calmly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
