import dayjs from 'dayjs/esm';
import { IFinancialParameterType } from 'app/entities/financial-parameter-type/financial-parameter-type.model';
import { ICountry } from 'app/entities/country/country.model';
import { IUsers } from 'app/entities/users/users.model';
import { IUserRole } from 'app/entities/user-role/user-role.model';

export interface IFinancialParameter {
  id: number;
  name?: string | null;
  value?: number | null;
  active?: boolean | null;
  mandatory?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  financialParameterType?: Pick<IFinancialParameterType, 'id'> | null;
  country?: Pick<ICountry, 'id'> | null;
  administrator?: Pick<IUsers, 'id'> | null;
  roleAuthorizeds?: Pick<IUserRole, 'id'>[] | null;
}

export type NewFinancialParameter = Omit<IFinancialParameter, 'id'> & { id: null };
