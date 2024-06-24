import dayjs from 'dayjs/esm';
import { IBudget } from 'app/entities/budget/budget.model';
import { IUserRole } from 'app/entities/user-role/user-role.model';

export interface IUsers {
  id: number;
  name?: string | null;
  email?: string | null;
  password?: string | null;
  active?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  userRoles?: Pick<IUserRole, 'id'>[] | null;
  budgetAuthorizeds?: Pick<IBudget, 'id'>[] | null;
}

export type NewUsers = Omit<IUsers, 'id'> & { id: null };
