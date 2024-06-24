import dayjs from 'dayjs/esm';
import { IDroolsRuleFile } from 'app/entities/drools-rule-file/drools-rule-file.model';

export interface IRuleAssignment {
  id: number;
  entityName?: string | null;
  entityId?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  droolsRuleFile?: Pick<IDroolsRuleFile, 'id'> | null;
}

export type NewRuleAssignment = Omit<IRuleAssignment, 'id'> & { id: null };
