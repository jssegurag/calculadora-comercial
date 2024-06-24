import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFinancialParameter, NewFinancialParameter } from '../financial-parameter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFinancialParameter for edit and NewFinancialParameterFormGroupInput for create.
 */
type FinancialParameterFormGroupInput = IFinancialParameter | PartialWithRequiredKeyOf<NewFinancialParameter>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFinancialParameter | NewFinancialParameter> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type FinancialParameterFormRawValue = FormValueOf<IFinancialParameter>;

type NewFinancialParameterFormRawValue = FormValueOf<NewFinancialParameter>;

type FinancialParameterFormDefaults = Pick<
  NewFinancialParameter,
  'id' | 'active' | 'mandatory' | 'createdDate' | 'lastModifiedDate' | 'roleAuthorizeds'
>;

type FinancialParameterFormGroupContent = {
  id: FormControl<FinancialParameterFormRawValue['id'] | NewFinancialParameter['id']>;
  name: FormControl<FinancialParameterFormRawValue['name']>;
  value: FormControl<FinancialParameterFormRawValue['value']>;
  active: FormControl<FinancialParameterFormRawValue['active']>;
  mandatory: FormControl<FinancialParameterFormRawValue['mandatory']>;
  createdBy: FormControl<FinancialParameterFormRawValue['createdBy']>;
  createdDate: FormControl<FinancialParameterFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<FinancialParameterFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<FinancialParameterFormRawValue['lastModifiedDate']>;
  financialParameterType: FormControl<FinancialParameterFormRawValue['financialParameterType']>;
  country: FormControl<FinancialParameterFormRawValue['country']>;
  administrator: FormControl<FinancialParameterFormRawValue['administrator']>;
  roleAuthorizeds: FormControl<FinancialParameterFormRawValue['roleAuthorizeds']>;
};

export type FinancialParameterFormGroup = FormGroup<FinancialParameterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FinancialParameterFormService {
  createFinancialParameterFormGroup(financialParameter: FinancialParameterFormGroupInput = { id: null }): FinancialParameterFormGroup {
    const financialParameterRawValue = this.convertFinancialParameterToFinancialParameterRawValue({
      ...this.getFormDefaults(),
      ...financialParameter,
    });
    return new FormGroup<FinancialParameterFormGroupContent>({
      id: new FormControl(
        { value: financialParameterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(financialParameterRawValue.name, {
        validators: [Validators.required],
      }),
      value: new FormControl(financialParameterRawValue.value, {
        validators: [Validators.required],
      }),
      active: new FormControl(financialParameterRawValue.active, {
        validators: [Validators.required],
      }),
      mandatory: new FormControl(financialParameterRawValue.mandatory, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(financialParameterRawValue.createdBy),
      createdDate: new FormControl(financialParameterRawValue.createdDate),
      lastModifiedBy: new FormControl(financialParameterRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(financialParameterRawValue.lastModifiedDate),
      financialParameterType: new FormControl(financialParameterRawValue.financialParameterType),
      country: new FormControl(financialParameterRawValue.country),
      administrator: new FormControl(financialParameterRawValue.administrator),
      roleAuthorizeds: new FormControl(financialParameterRawValue.roleAuthorizeds ?? []),
    });
  }

  getFinancialParameter(form: FinancialParameterFormGroup): IFinancialParameter | NewFinancialParameter {
    return this.convertFinancialParameterRawValueToFinancialParameter(
      form.getRawValue() as FinancialParameterFormRawValue | NewFinancialParameterFormRawValue,
    );
  }

  resetForm(form: FinancialParameterFormGroup, financialParameter: FinancialParameterFormGroupInput): void {
    const financialParameterRawValue = this.convertFinancialParameterToFinancialParameterRawValue({
      ...this.getFormDefaults(),
      ...financialParameter,
    });
    form.reset(
      {
        ...financialParameterRawValue,
        id: { value: financialParameterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FinancialParameterFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      mandatory: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
      roleAuthorizeds: [],
    };
  }

  private convertFinancialParameterRawValueToFinancialParameter(
    rawFinancialParameter: FinancialParameterFormRawValue | NewFinancialParameterFormRawValue,
  ): IFinancialParameter | NewFinancialParameter {
    return {
      ...rawFinancialParameter,
      createdDate: dayjs(rawFinancialParameter.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawFinancialParameter.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFinancialParameterToFinancialParameterRawValue(
    financialParameter: IFinancialParameter | (Partial<NewFinancialParameter> & FinancialParameterFormDefaults),
  ): FinancialParameterFormRawValue | PartialWithRequiredKeyOf<NewFinancialParameterFormRawValue> {
    return {
      ...financialParameter,
      createdDate: financialParameter.createdDate ? financialParameter.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: financialParameter.lastModifiedDate ? financialParameter.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      roleAuthorizeds: financialParameter.roleAuthorizeds ?? [],
    };
  }
}
