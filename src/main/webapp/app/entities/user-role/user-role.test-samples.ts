import dayjs from 'dayjs/esm';

import { IUserRole, NewUserRole } from './user-role.model';

export const sampleWithRequiredData: IUserRole = {
  id: 4018,
  name: 'towards bashfully',
};

export const sampleWithPartialData: IUserRole = {
  id: 3211,
  name: 'augur eyeglasses meanwhile',
  createdBy: 'upon surprisingly some',
  lastModifiedBy: 'to decide',
  lastModifiedDate: dayjs('2024-06-24T01:16'),
};

export const sampleWithFullData: IUserRole = {
  id: 6949,
  name: 'how redraft',
  createdBy: 'rib gosh gosh',
  createdDate: dayjs('2024-06-24T09:18'),
  lastModifiedBy: 'freewheel upside-down',
  lastModifiedDate: dayjs('2024-06-24T05:46'),
};

export const sampleWithNewData: NewUserRole = {
  name: 'likewise',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
