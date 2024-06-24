import dayjs from 'dayjs/esm';

import { IFinancialParameterType, NewFinancialParameterType } from './financial-parameter-type.model';

export const sampleWithRequiredData: IFinancialParameterType = {
  id: 8640,
  name: 'yahoo who foot',
  active: true,
};

export const sampleWithPartialData: IFinancialParameterType = {
  id: 4519,
  name: 'witty blah',
  active: false,
  createdDate: dayjs('2024-06-24T11:51'),
  lastModifiedDate: dayjs('2024-06-23T16:33'),
};

export const sampleWithFullData: IFinancialParameterType = {
  id: 30846,
  name: 'before contribute during',
  active: true,
  createdBy: 'noisily',
  createdDate: dayjs('2024-06-24T04:44'),
  lastModifiedBy: 'townhouse breakable upside-down',
  lastModifiedDate: dayjs('2024-06-24T07:56'),
};

export const sampleWithNewData: NewFinancialParameterType = {
  name: 'thoroughly mutilate',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
