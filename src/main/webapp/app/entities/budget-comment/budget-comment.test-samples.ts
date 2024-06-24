import dayjs from 'dayjs/esm';

import { IBudgetComment, NewBudgetComment } from './budget-comment.model';

export const sampleWithRequiredData: IBudgetComment = {
  id: 16988,
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IBudgetComment = {
  id: 32500,
  content: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2024-06-24T11:26'),
  lastModifiedBy: 'so',
};

export const sampleWithFullData: IBudgetComment = {
  id: 32766,
  content: '../fake-data/blob/hipster.txt',
  createdBy: 'bustling',
  createdDate: dayjs('2024-06-24T05:07'),
  lastModifiedBy: 'stud because nursery',
  lastModifiedDate: dayjs('2024-06-23T21:37'),
};

export const sampleWithNewData: NewBudgetComment = {
  content: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
