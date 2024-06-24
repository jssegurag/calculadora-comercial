import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPermission, NewPermission } from '../permission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPermission for edit and NewPermissionFormGroupInput for create.
 */
type PermissionFormGroupInput = IPermission | PartialWithRequiredKeyOf<NewPermission>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPermission | NewPermission> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type PermissionFormRawValue = FormValueOf<IPermission>;

type NewPermissionFormRawValue = FormValueOf<NewPermission>;

type PermissionFormDefaults = Pick<NewPermission, 'id' | 'createdDate' | 'lastModifiedDate' | 'permissions'>;

type PermissionFormGroupContent = {
  id: FormControl<PermissionFormRawValue['id'] | NewPermission['id']>;
  name: FormControl<PermissionFormRawValue['name']>;
  description: FormControl<PermissionFormRawValue['description']>;
  createdBy: FormControl<PermissionFormRawValue['createdBy']>;
  createdDate: FormControl<PermissionFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<PermissionFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<PermissionFormRawValue['lastModifiedDate']>;
  permissions: FormControl<PermissionFormRawValue['permissions']>;
};

export type PermissionFormGroup = FormGroup<PermissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PermissionFormService {
  createPermissionFormGroup(permission: PermissionFormGroupInput = { id: null }): PermissionFormGroup {
    const permissionRawValue = this.convertPermissionToPermissionRawValue({
      ...this.getFormDefaults(),
      ...permission,
    });
    return new FormGroup<PermissionFormGroupContent>({
      id: new FormControl(
        { value: permissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(permissionRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(permissionRawValue.description),
      createdBy: new FormControl(permissionRawValue.createdBy),
      createdDate: new FormControl(permissionRawValue.createdDate),
      lastModifiedBy: new FormControl(permissionRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(permissionRawValue.lastModifiedDate),
      permissions: new FormControl(permissionRawValue.permissions ?? []),
    });
  }

  getPermission(form: PermissionFormGroup): IPermission | NewPermission {
    return this.convertPermissionRawValueToPermission(form.getRawValue() as PermissionFormRawValue | NewPermissionFormRawValue);
  }

  resetForm(form: PermissionFormGroup, permission: PermissionFormGroupInput): void {
    const permissionRawValue = this.convertPermissionToPermissionRawValue({ ...this.getFormDefaults(), ...permission });
    form.reset(
      {
        ...permissionRawValue,
        id: { value: permissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PermissionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
      permissions: [],
    };
  }

  private convertPermissionRawValueToPermission(
    rawPermission: PermissionFormRawValue | NewPermissionFormRawValue,
  ): IPermission | NewPermission {
    return {
      ...rawPermission,
      createdDate: dayjs(rawPermission.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawPermission.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPermissionToPermissionRawValue(
    permission: IPermission | (Partial<NewPermission> & PermissionFormDefaults),
  ): PermissionFormRawValue | PartialWithRequiredKeyOf<NewPermissionFormRawValue> {
    return {
      ...permission,
      createdDate: permission.createdDate ? permission.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: permission.lastModifiedDate ? permission.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      permissions: permission.permissions ?? [],
    };
  }
}
