import dayjs from 'dayjs/esm';

import { IFinancialParameter, NewFinancialParameter } from './financial-parameter.model';

export const sampleWithRequiredData: IFinancialParameter = {
  id: 10524,
  name: 'solidly vineyard chestnut',
  value: 26601.38,
  active: true,
  mandatory: false,
};

export const sampleWithPartialData: IFinancialParameter = {
  id: 31727,
  name: 'sieve',
  value: 6568.38,
  active: false,
  mandatory: true,
  createdBy: 'sans sympathetically caring',
  lastModifiedBy: 'anonymise for ew',
};

export const sampleWithFullData: IFinancialParameter = {
  id: 2527,
  name: 'even outlive',
  value: 27810.45,
  active: false,
  mandatory: false,
  createdBy: 'specialize poach besides',
  createdDate: dayjs('2024-06-23T22:39'),
  lastModifiedBy: 'stock if',
  lastModifiedDate: dayjs('2024-06-24T04:53'),
};

export const sampleWithNewData: NewFinancialParameter = {
  name: 'for lipoprotein ha',
  value: 24976.59,
  active: true,
  mandatory: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
