import dayjs from 'dayjs/esm';

import { IPosition, NewPosition } from './position.model';

export const sampleWithRequiredData: IPosition = {
  id: 10120,
  name: 'hmph boohoo',
  active: true,
};

export const sampleWithPartialData: IPosition = {
  id: 5955,
  name: 'aw while plump',
  active: true,
  lastModifiedDate: dayjs('2024-06-24T08:18'),
};

export const sampleWithFullData: IPosition = {
  id: 30900,
  name: 'and',
  active: true,
  createdBy: 'snowflake as but',
  createdDate: dayjs('2024-06-23T23:10'),
  lastModifiedBy: 'at',
  lastModifiedDate: dayjs('2024-06-24T10:22'),
};

export const sampleWithNewData: NewPosition = {
  name: 'whoever',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
