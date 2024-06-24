import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPosition, NewPosition } from '../position.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPosition for edit and NewPositionFormGroupInput for create.
 */
type PositionFormGroupInput = IPosition | PartialWithRequiredKeyOf<NewPosition>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPosition | NewPosition> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type PositionFormRawValue = FormValueOf<IPosition>;

type NewPositionFormRawValue = FormValueOf<NewPosition>;

type PositionFormDefaults = Pick<NewPosition, 'id' | 'active' | 'createdDate' | 'lastModifiedDate'>;

type PositionFormGroupContent = {
  id: FormControl<PositionFormRawValue['id'] | NewPosition['id']>;
  name: FormControl<PositionFormRawValue['name']>;
  active: FormControl<PositionFormRawValue['active']>;
  createdBy: FormControl<PositionFormRawValue['createdBy']>;
  createdDate: FormControl<PositionFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<PositionFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<PositionFormRawValue['lastModifiedDate']>;
};

export type PositionFormGroup = FormGroup<PositionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PositionFormService {
  createPositionFormGroup(position: PositionFormGroupInput = { id: null }): PositionFormGroup {
    const positionRawValue = this.convertPositionToPositionRawValue({
      ...this.getFormDefaults(),
      ...position,
    });
    return new FormGroup<PositionFormGroupContent>({
      id: new FormControl(
        { value: positionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(positionRawValue.name, {
        validators: [Validators.required],
      }),
      active: new FormControl(positionRawValue.active, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(positionRawValue.createdBy),
      createdDate: new FormControl(positionRawValue.createdDate),
      lastModifiedBy: new FormControl(positionRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(positionRawValue.lastModifiedDate),
    });
  }

  getPosition(form: PositionFormGroup): IPosition | NewPosition {
    return this.convertPositionRawValueToPosition(form.getRawValue() as PositionFormRawValue | NewPositionFormRawValue);
  }

  resetForm(form: PositionFormGroup, position: PositionFormGroupInput): void {
    const positionRawValue = this.convertPositionToPositionRawValue({ ...this.getFormDefaults(), ...position });
    form.reset(
      {
        ...positionRawValue,
        id: { value: positionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PositionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertPositionRawValueToPosition(rawPosition: PositionFormRawValue | NewPositionFormRawValue): IPosition | NewPosition {
    return {
      ...rawPosition,
      createdDate: dayjs(rawPosition.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawPosition.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPositionToPositionRawValue(
    position: IPosition | (Partial<NewPosition> & PositionFormDefaults),
  ): PositionFormRawValue | PartialWithRequiredKeyOf<NewPositionFormRawValue> {
    return {
      ...position,
      createdDate: position.createdDate ? position.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: position.lastModifiedDate ? position.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
