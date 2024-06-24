import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDroolsRuleFile, NewDroolsRuleFile } from '../drools-rule-file.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDroolsRuleFile for edit and NewDroolsRuleFileFormGroupInput for create.
 */
type DroolsRuleFileFormGroupInput = IDroolsRuleFile | PartialWithRequiredKeyOf<NewDroolsRuleFile>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDroolsRuleFile | NewDroolsRuleFile> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type DroolsRuleFileFormRawValue = FormValueOf<IDroolsRuleFile>;

type NewDroolsRuleFileFormRawValue = FormValueOf<NewDroolsRuleFile>;

type DroolsRuleFileFormDefaults = Pick<NewDroolsRuleFile, 'id' | 'active' | 'createdDate' | 'lastModifiedDate'>;

type DroolsRuleFileFormGroupContent = {
  id: FormControl<DroolsRuleFileFormRawValue['id'] | NewDroolsRuleFile['id']>;
  fileName: FormControl<DroolsRuleFileFormRawValue['fileName']>;
  fileContent: FormControl<DroolsRuleFileFormRawValue['fileContent']>;
  description: FormControl<DroolsRuleFileFormRawValue['description']>;
  active: FormControl<DroolsRuleFileFormRawValue['active']>;
  createdBy: FormControl<DroolsRuleFileFormRawValue['createdBy']>;
  createdDate: FormControl<DroolsRuleFileFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<DroolsRuleFileFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<DroolsRuleFileFormRawValue['lastModifiedDate']>;
};

export type DroolsRuleFileFormGroup = FormGroup<DroolsRuleFileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DroolsRuleFileFormService {
  createDroolsRuleFileFormGroup(droolsRuleFile: DroolsRuleFileFormGroupInput = { id: null }): DroolsRuleFileFormGroup {
    const droolsRuleFileRawValue = this.convertDroolsRuleFileToDroolsRuleFileRawValue({
      ...this.getFormDefaults(),
      ...droolsRuleFile,
    });
    return new FormGroup<DroolsRuleFileFormGroupContent>({
      id: new FormControl(
        { value: droolsRuleFileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fileName: new FormControl(droolsRuleFileRawValue.fileName, {
        validators: [Validators.required],
      }),
      fileContent: new FormControl(droolsRuleFileRawValue.fileContent, {
        validators: [Validators.required],
      }),
      description: new FormControl(droolsRuleFileRawValue.description),
      active: new FormControl(droolsRuleFileRawValue.active, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(droolsRuleFileRawValue.createdBy),
      createdDate: new FormControl(droolsRuleFileRawValue.createdDate),
      lastModifiedBy: new FormControl(droolsRuleFileRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(droolsRuleFileRawValue.lastModifiedDate),
    });
  }

  getDroolsRuleFile(form: DroolsRuleFileFormGroup): IDroolsRuleFile | NewDroolsRuleFile {
    return this.convertDroolsRuleFileRawValueToDroolsRuleFile(
      form.getRawValue() as DroolsRuleFileFormRawValue | NewDroolsRuleFileFormRawValue,
    );
  }

  resetForm(form: DroolsRuleFileFormGroup, droolsRuleFile: DroolsRuleFileFormGroupInput): void {
    const droolsRuleFileRawValue = this.convertDroolsRuleFileToDroolsRuleFileRawValue({ ...this.getFormDefaults(), ...droolsRuleFile });
    form.reset(
      {
        ...droolsRuleFileRawValue,
        id: { value: droolsRuleFileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DroolsRuleFileFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertDroolsRuleFileRawValueToDroolsRuleFile(
    rawDroolsRuleFile: DroolsRuleFileFormRawValue | NewDroolsRuleFileFormRawValue,
  ): IDroolsRuleFile | NewDroolsRuleFile {
    return {
      ...rawDroolsRuleFile,
      createdDate: dayjs(rawDroolsRuleFile.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawDroolsRuleFile.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDroolsRuleFileToDroolsRuleFileRawValue(
    droolsRuleFile: IDroolsRuleFile | (Partial<NewDroolsRuleFile> & DroolsRuleFileFormDefaults),
  ): DroolsRuleFileFormRawValue | PartialWithRequiredKeyOf<NewDroolsRuleFileFormRawValue> {
    return {
      ...droolsRuleFile,
      createdDate: droolsRuleFile.createdDate ? droolsRuleFile.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: droolsRuleFile.lastModifiedDate ? droolsRuleFile.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
