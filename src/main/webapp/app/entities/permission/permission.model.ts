import dayjs from 'dayjs/esm';
import { IUserRole } from 'app/entities/user-role/user-role.model';

export interface IPermission {
  id: number;
  name?: string | null;
  description?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  permissions?: Pick<IUserRole, 'id'>[] | null;
}

export type NewPermission = Omit<IPermission, 'id'> & { id: null };
