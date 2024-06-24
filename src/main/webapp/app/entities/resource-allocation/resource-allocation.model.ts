import dayjs from 'dayjs/esm';
import { IBudget } from 'app/entities/budget/budget.model';
import { IResource } from 'app/entities/resource/resource.model';
import { IBudgetTemplate } from 'app/entities/budget-template/budget-template.model';

export interface IResourceAllocation {
  id: number;
  assignedHours?: number | null;
  totalCost?: number | null;
  units?: number | null;
  capacity?: number | null;
  plannedHours?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  budget?: Pick<IBudget, 'id'> | null;
  resource?: Pick<IResource, 'id'> | null;
  budgetTemplate?: Pick<IBudgetTemplate, 'id'> | null;
}

export type NewResourceAllocation = Omit<IResourceAllocation, 'id'> & { id: null };
