import dayjs from 'dayjs/esm';

import { IDroolsRuleFile, NewDroolsRuleFile } from './drools-rule-file.model';

export const sampleWithRequiredData: IDroolsRuleFile = {
  id: 12928,
  fileName: 'mid willfully compliment',
  fileContent: '../fake-data/blob/hipster.txt',
  active: true,
};

export const sampleWithPartialData: IDroolsRuleFile = {
  id: 31978,
  fileName: 'whether',
  fileContent: '../fake-data/blob/hipster.txt',
  active: true,
  createdBy: 'shopping crisp gaffe',
  createdDate: dayjs('2024-06-24T13:00'),
  lastModifiedBy: 'along sturdy',
};

export const sampleWithFullData: IDroolsRuleFile = {
  id: 21091,
  fileName: 'great north deskill',
  fileContent: '../fake-data/blob/hipster.txt',
  description: 'acceptable',
  active: true,
  createdBy: 'off knottily above',
  createdDate: dayjs('2024-06-23T19:07'),
  lastModifiedBy: 'unless till whoever',
  lastModifiedDate: dayjs('2024-06-24T02:06'),
};

export const sampleWithNewData: NewDroolsRuleFile = {
  fileName: 'on than',
  fileContent: '../fake-data/blob/hipster.txt',
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
