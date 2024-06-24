import dayjs from 'dayjs/esm';

export interface IDroolsRuleFile {
  id: number;
  fileName?: string | null;
  fileContent?: string | null;
  description?: string | null;
  active?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewDroolsRuleFile = Omit<IDroolsRuleFile, 'id'> & { id: null };
