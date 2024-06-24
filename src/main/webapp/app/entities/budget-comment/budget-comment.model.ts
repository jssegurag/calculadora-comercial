import dayjs from 'dayjs/esm';
import { IBudget } from 'app/entities/budget/budget.model';

export interface IBudgetComment {
  id: number;
  content?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  budget?: Pick<IBudget, 'id'> | null;
}

export type NewBudgetComment = Omit<IBudgetComment, 'id'> & { id: null };
