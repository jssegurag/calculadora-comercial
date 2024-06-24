import dayjs from 'dayjs/esm';

import { IBudgetTemplate, NewBudgetTemplate } from './budget-template.model';

export const sampleWithRequiredData: IBudgetTemplate = {
  id: 18091,
  name: 'yowza',
  active: true,
};

export const sampleWithPartialData: IBudgetTemplate = {
  id: 30490,
  name: 'huzzah amid for',
  description: '../fake-data/blob/hipster.txt',
  monthlyHours: 2871.46,
  plannedHours: 27788.56,
  resourceCount: 23809,
  income: 32517.63,
  grossProfitRule: 18650.02,
  absorbedFixedCosts: 16500.84,
  otherExpenses: 7933.74,
  estimatedTaxes: 13074.87,
  estimatedNetProfit: 20038.9,
  netMarginPercentage: 6699.2,
  netMarginRule: 14978.39,
  active: false,
  createdBy: 'inconsequential which ravish',
  createdDate: dayjs('2024-06-23T23:17'),
};

export const sampleWithFullData: IBudgetTemplate = {
  id: 32748,
  name: 'instruction boolean even',
  description: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2024-06-24'),
  endDate: dayjs('2024-06-24'),
  estimatedDurationDays: 32163,
  durationMonths: 13332,
  monthlyHours: 7099.66,
  plannedHours: 12197.1,
  resourceCount: 26697,
  income: 6087.97,
  otherTaxes: 18851.33,
  descriptionOtherTaxes: 'zowie helpless diligently',
  withholdingTaxes: 26831.43,
  modAndCifCosts: 12407.78,
  grossProfit: 1204.24,
  grossProfitPercentage: 17226.3,
  grossProfitRule: 11780.5,
  absorbedFixedCosts: 4567.82,
  otherExpenses: 25616.37,
  profitBeforeTax: 23931.43,
  estimatedTaxes: 21453.63,
  estimatedNetProfit: 5940.24,
  netMarginPercentage: 30424.9,
  netMarginRule: 6858.15,
  commissionToReceive: 10560.79,
  active: false,
  createdBy: 'where',
  createdDate: dayjs('2024-06-24T14:09'),
  lastModifiedBy: 'skinny',
  lastModifiedDate: dayjs('2024-06-24T09:07'),
};

export const sampleWithNewData: NewBudgetTemplate = {
  name: 'hastily anklet however',
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
