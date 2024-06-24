import dayjs from 'dayjs/esm';

import { IRuleAssignment, NewRuleAssignment } from './rule-assignment.model';

export const sampleWithRequiredData: IRuleAssignment = {
  id: 14400,
  entityName: 'glossy snort',
  entityId: 10390,
};

export const sampleWithPartialData: IRuleAssignment = {
  id: 17075,
  entityName: 'driveway before',
  entityId: 2003,
};

export const sampleWithFullData: IRuleAssignment = {
  id: 14608,
  entityName: 'definite',
  entityId: 11248,
  createdBy: 'as whoa',
  createdDate: dayjs('2024-06-24T08:33'),
  lastModifiedBy: 'inside considering yum',
  lastModifiedDate: dayjs('2024-06-23T21:32'),
};

export const sampleWithNewData: NewRuleAssignment = {
  entityName: 'uh-huh',
  entityId: 11476,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
