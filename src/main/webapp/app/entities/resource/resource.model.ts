import dayjs from 'dayjs/esm';
import { IPosition } from 'app/entities/position/position.model';

export interface IResource {
  id: number;
  salary?: number | null;
  hourlyRate?: number | null;
  active?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  position?: Pick<IPosition, 'id'> | null;
}

export type NewResource = Omit<IResource, 'id'> & { id: null };
