import dayjs from 'dayjs/esm';
import { ICountry } from 'app/entities/country/country.model';

export interface IBudgetTemplate {
  id: number;
  name?: string | null;
  description?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  estimatedDurationDays?: number | null;
  durationMonths?: number | null;
  monthlyHours?: number | null;
  plannedHours?: number | null;
  resourceCount?: number | null;
  income?: number | null;
  otherTaxes?: number | null;
  descriptionOtherTaxes?: string | null;
  withholdingTaxes?: number | null;
  modAndCifCosts?: number | null;
  grossProfit?: number | null;
  grossProfitPercentage?: number | null;
  grossProfitRule?: number | null;
  absorbedFixedCosts?: number | null;
  otherExpenses?: number | null;
  profitBeforeTax?: number | null;
  estimatedTaxes?: number | null;
  estimatedNetProfit?: number | null;
  netMarginPercentage?: number | null;
  netMarginRule?: number | null;
  commissionToReceive?: number | null;
  active?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  country?: Pick<ICountry, 'id'> | null;
}

export type NewBudgetTemplate = Omit<IBudgetTemplate, 'id'> & { id: null };
