import dayjs from 'dayjs/esm';

import { IBudget, NewBudget } from './budget.model';

export const sampleWithRequiredData: IBudget = {
  id: 28816,
  name: 'sousaphone',
};

export const sampleWithPartialData: IBudget = {
  id: 8217,
  name: 'hobbit fiercely nearly',
  description: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2024-06-24'),
  estimatedDurationDays: 26601,
  monthlyHours: 15009.67,
  plannedHours: 31325.17,
  modAndCifCosts: 23685.52,
  grossProfitPercentage: 13993.41,
  absorbedFixedCosts: 4257.62,
  estimatedTaxes: 11597.19,
  estimatedNetProfit: 26980.61,
  netMarginRule: 4391.51,
  commissionToReceive: 16056.2,
  approvalTime: dayjs('2024-06-23T22:00'),
  approvalStatus: 'rosemary',
  createdBy: 'during sometimes apud',
};

export const sampleWithFullData: IBudget = {
  id: 17642,
  name: 'so',
  description: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2024-06-24'),
  endDate: dayjs('2024-06-24'),
  estimatedDurationDays: 10834,
  durationMonths: 2271,
  monthlyHours: 4360.39,
  plannedHours: 4514.38,
  resourceCount: 26669,
  income: 23230.54,
  otherTaxes: 17287.01,
  descriptionOtherTaxes: 'pooh silky dot',
  withholdingTaxes: 4250.54,
  modAndCifCosts: 11439.57,
  grossProfit: 24713.62,
  grossProfitPercentage: 2693.45,
  grossProfitRule: 6125.55,
  absorbedFixedCosts: 26870.12,
  otherExpenses: 22132.79,
  profitBeforeTax: 24915.37,
  estimatedTaxes: 14877.32,
  estimatedNetProfit: 18676.44,
  netMarginPercentage: 26950.28,
  netMarginRule: 20733.14,
  commissionToReceive: 2427.1,
  needsApproval: true,
  approvalDecision: 'toward',
  approvalDate: dayjs('2024-06-23T18:03'),
  approvalTime: dayjs('2024-06-24T13:24'),
  approvalComments: 'meh',
  approvalStatus: 'deliquesce',
  createdBy: 'joyful consequently',
  createdDate: dayjs('2024-06-24T09:20'),
  lastModifiedBy: 'butt',
  lastModifiedDate: dayjs('2024-06-23T22:52'),
};

export const sampleWithNewData: NewBudget = {
  name: 'blah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
