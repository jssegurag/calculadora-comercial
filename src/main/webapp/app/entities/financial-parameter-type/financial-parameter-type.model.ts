import dayjs from 'dayjs/esm';

export interface IFinancialParameterType {
  id: number;
  name?: string | null;
  active?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewFinancialParameterType = Omit<IFinancialParameterType, 'id'> & { id: null };
