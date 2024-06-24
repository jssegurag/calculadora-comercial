import dayjs from 'dayjs/esm';
import { IPermission } from 'app/entities/permission/permission.model';
import { IBudget } from 'app/entities/budget/budget.model';
import { IFinancialParameter } from 'app/entities/financial-parameter/financial-parameter.model';
import { IUsers } from 'app/entities/users/users.model';

export interface IUserRole {
  id: number;
  name?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  permissions?: Pick<IPermission, 'id'>[] | null;
  budgets?: Pick<IBudget, 'id'>[] | null;
  financialParameters?: Pick<IFinancialParameter, 'id'>[] | null;
  users?: Pick<IUsers, 'id'>[] | null;
}

export type NewUserRole = Omit<IUserRole, 'id'> & { id: null };
