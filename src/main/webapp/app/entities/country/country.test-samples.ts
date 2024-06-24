import dayjs from 'dayjs/esm';

import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 8860,
  name: 'microwave testimonial',
  active: false,
};

export const sampleWithPartialData: ICountry = {
  id: 6425,
  name: 'outlandish',
  active: false,
};

export const sampleWithFullData: ICountry = {
  id: 15704,
  name: 'daintily exclaim',
  active: false,
  createdBy: 'empathize ack',
  createdDate: dayjs('2024-06-23T21:31'),
  lastModifiedBy: 'triumphantly high',
  lastModifiedDate: dayjs('2024-06-23T18:23'),
};

export const sampleWithNewData: NewCountry = {
  name: 'intellect',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
