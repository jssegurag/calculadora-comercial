import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFinancialParameterType, NewFinancialParameterType } from '../financial-parameter-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFinancialParameterType for edit and NewFinancialParameterTypeFormGroupInput for create.
 */
type FinancialParameterTypeFormGroupInput = IFinancialParameterType | PartialWithRequiredKeyOf<NewFinancialParameterType>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFinancialParameterType | NewFinancialParameterType> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type FinancialParameterTypeFormRawValue = FormValueOf<IFinancialParameterType>;

type NewFinancialParameterTypeFormRawValue = FormValueOf<NewFinancialParameterType>;

type FinancialParameterTypeFormDefaults = Pick<NewFinancialParameterType, 'id' | 'active' | 'createdDate' | 'lastModifiedDate'>;

type FinancialParameterTypeFormGroupContent = {
  id: FormControl<FinancialParameterTypeFormRawValue['id'] | NewFinancialParameterType['id']>;
  name: FormControl<FinancialParameterTypeFormRawValue['name']>;
  active: FormControl<FinancialParameterTypeFormRawValue['active']>;
  createdBy: FormControl<FinancialParameterTypeFormRawValue['createdBy']>;
  createdDate: FormControl<FinancialParameterTypeFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<FinancialParameterTypeFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<FinancialParameterTypeFormRawValue['lastModifiedDate']>;
};

export type FinancialParameterTypeFormGroup = FormGroup<FinancialParameterTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FinancialParameterTypeFormService {
  createFinancialParameterTypeFormGroup(
    financialParameterType: FinancialParameterTypeFormGroupInput = { id: null },
  ): FinancialParameterTypeFormGroup {
    const financialParameterTypeRawValue = this.convertFinancialParameterTypeToFinancialParameterTypeRawValue({
      ...this.getFormDefaults(),
      ...financialParameterType,
    });
    return new FormGroup<FinancialParameterTypeFormGroupContent>({
      id: new FormControl(
        { value: financialParameterTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(financialParameterTypeRawValue.name, {
        validators: [Validators.required],
      }),
      active: new FormControl(financialParameterTypeRawValue.active, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(financialParameterTypeRawValue.createdBy),
      createdDate: new FormControl(financialParameterTypeRawValue.createdDate),
      lastModifiedBy: new FormControl(financialParameterTypeRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(financialParameterTypeRawValue.lastModifiedDate),
    });
  }

  getFinancialParameterType(form: FinancialParameterTypeFormGroup): IFinancialParameterType | NewFinancialParameterType {
    return this.convertFinancialParameterTypeRawValueToFinancialParameterType(
      form.getRawValue() as FinancialParameterTypeFormRawValue | NewFinancialParameterTypeFormRawValue,
    );
  }

  resetForm(form: FinancialParameterTypeFormGroup, financialParameterType: FinancialParameterTypeFormGroupInput): void {
    const financialParameterTypeRawValue = this.convertFinancialParameterTypeToFinancialParameterTypeRawValue({
      ...this.getFormDefaults(),
      ...financialParameterType,
    });
    form.reset(
      {
        ...financialParameterTypeRawValue,
        id: { value: financialParameterTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FinancialParameterTypeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertFinancialParameterTypeRawValueToFinancialParameterType(
    rawFinancialParameterType: FinancialParameterTypeFormRawValue | NewFinancialParameterTypeFormRawValue,
  ): IFinancialParameterType | NewFinancialParameterType {
    return {
      ...rawFinancialParameterType,
      createdDate: dayjs(rawFinancialParameterType.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawFinancialParameterType.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFinancialParameterTypeToFinancialParameterTypeRawValue(
    financialParameterType: IFinancialParameterType | (Partial<NewFinancialParameterType> & FinancialParameterTypeFormDefaults),
  ): FinancialParameterTypeFormRawValue | PartialWithRequiredKeyOf<NewFinancialParameterTypeFormRawValue> {
    return {
      ...financialParameterType,
      createdDate: financialParameterType.createdDate ? financialParameterType.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: financialParameterType.lastModifiedDate
        ? financialParameterType.lastModifiedDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
