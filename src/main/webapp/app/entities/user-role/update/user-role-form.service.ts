import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserRole, NewUserRole } from '../user-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserRole for edit and NewUserRoleFormGroupInput for create.
 */
type UserRoleFormGroupInput = IUserRole | PartialWithRequiredKeyOf<NewUserRole>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserRole | NewUserRole> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type UserRoleFormRawValue = FormValueOf<IUserRole>;

type NewUserRoleFormRawValue = FormValueOf<NewUserRole>;

type UserRoleFormDefaults = Pick<
  NewUserRole,
  'id' | 'createdDate' | 'lastModifiedDate' | 'permissions' | 'budgets' | 'financialParameters' | 'users'
>;

type UserRoleFormGroupContent = {
  id: FormControl<UserRoleFormRawValue['id'] | NewUserRole['id']>;
  name: FormControl<UserRoleFormRawValue['name']>;
  createdBy: FormControl<UserRoleFormRawValue['createdBy']>;
  createdDate: FormControl<UserRoleFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<UserRoleFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<UserRoleFormRawValue['lastModifiedDate']>;
  permissions: FormControl<UserRoleFormRawValue['permissions']>;
  budgets: FormControl<UserRoleFormRawValue['budgets']>;
  financialParameters: FormControl<UserRoleFormRawValue['financialParameters']>;
  users: FormControl<UserRoleFormRawValue['users']>;
};

export type UserRoleFormGroup = FormGroup<UserRoleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserRoleFormService {
  createUserRoleFormGroup(userRole: UserRoleFormGroupInput = { id: null }): UserRoleFormGroup {
    const userRoleRawValue = this.convertUserRoleToUserRoleRawValue({
      ...this.getFormDefaults(),
      ...userRole,
    });
    return new FormGroup<UserRoleFormGroupContent>({
      id: new FormControl(
        { value: userRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(userRoleRawValue.name, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(userRoleRawValue.createdBy),
      createdDate: new FormControl(userRoleRawValue.createdDate),
      lastModifiedBy: new FormControl(userRoleRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(userRoleRawValue.lastModifiedDate),
      permissions: new FormControl(userRoleRawValue.permissions ?? []),
      budgets: new FormControl(userRoleRawValue.budgets ?? []),
      financialParameters: new FormControl(userRoleRawValue.financialParameters ?? []),
      users: new FormControl(userRoleRawValue.users ?? []),
    });
  }

  getUserRole(form: UserRoleFormGroup): IUserRole | NewUserRole {
    return this.convertUserRoleRawValueToUserRole(form.getRawValue() as UserRoleFormRawValue | NewUserRoleFormRawValue);
  }

  resetForm(form: UserRoleFormGroup, userRole: UserRoleFormGroupInput): void {
    const userRoleRawValue = this.convertUserRoleToUserRoleRawValue({ ...this.getFormDefaults(), ...userRole });
    form.reset(
      {
        ...userRoleRawValue,
        id: { value: userRoleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserRoleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
      permissions: [],
      budgets: [],
      financialParameters: [],
      users: [],
    };
  }

  private convertUserRoleRawValueToUserRole(rawUserRole: UserRoleFormRawValue | NewUserRoleFormRawValue): IUserRole | NewUserRole {
    return {
      ...rawUserRole,
      createdDate: dayjs(rawUserRole.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawUserRole.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserRoleToUserRoleRawValue(
    userRole: IUserRole | (Partial<NewUserRole> & UserRoleFormDefaults),
  ): UserRoleFormRawValue | PartialWithRequiredKeyOf<NewUserRoleFormRawValue> {
    return {
      ...userRole,
      createdDate: userRole.createdDate ? userRole.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: userRole.lastModifiedDate ? userRole.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      permissions: userRole.permissions ?? [],
      budgets: userRole.budgets ?? [],
      financialParameters: userRole.financialParameters ?? [],
      users: userRole.users ?? [],
    };
  }
}
