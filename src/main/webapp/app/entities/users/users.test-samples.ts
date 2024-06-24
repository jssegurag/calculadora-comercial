import dayjs from 'dayjs/esm';

import { IUsers, NewUsers } from './users.model';

export const sampleWithRequiredData: IUsers = {
  id: 25672,
  name: 'atop solid buy',
  email: 'Kathleen.Douglas54@yahoo.com',
  password: 'sensationalize',
  active: false,
};

export const sampleWithPartialData: IUsers = {
  id: 23859,
  name: 'icy indeed pro',
  email: 'Monroe.Luettgen17@yahoo.com',
  password: 'huzzah in opulent',
  active: false,
  createdDate: dayjs('2024-06-24T10:58'),
};

export const sampleWithFullData: IUsers = {
  id: 6069,
  name: 'straight enlightened',
  email: 'Charles.Mayer31@yahoo.com',
  password: 'false scarily',
  active: false,
  createdBy: 'excepting',
  createdDate: dayjs('2024-06-24T07:12'),
  lastModifiedBy: 'prudent',
  lastModifiedDate: dayjs('2024-06-24T05:40'),
};

export const sampleWithNewData: NewUsers = {
  name: 'after uh-huh',
  email: 'Heidi_Bogan@hotmail.com',
  password: 'yuck',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
