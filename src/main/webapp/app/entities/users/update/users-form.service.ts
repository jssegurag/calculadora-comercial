import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUsers, NewUsers } from '../users.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUsers for edit and NewUsersFormGroupInput for create.
 */
type UsersFormGroupInput = IUsers | PartialWithRequiredKeyOf<NewUsers>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUsers | NewUsers> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type UsersFormRawValue = FormValueOf<IUsers>;

type NewUsersFormRawValue = FormValueOf<NewUsers>;

type UsersFormDefaults = Pick<NewUsers, 'id' | 'active' | 'createdDate' | 'lastModifiedDate' | 'userRoles' | 'budgetAuthorizeds'>;

type UsersFormGroupContent = {
  id: FormControl<UsersFormRawValue['id'] | NewUsers['id']>;
  name: FormControl<UsersFormRawValue['name']>;
  email: FormControl<UsersFormRawValue['email']>;
  password: FormControl<UsersFormRawValue['password']>;
  active: FormControl<UsersFormRawValue['active']>;
  createdBy: FormControl<UsersFormRawValue['createdBy']>;
  createdDate: FormControl<UsersFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<UsersFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<UsersFormRawValue['lastModifiedDate']>;
  userRoles: FormControl<UsersFormRawValue['userRoles']>;
  budgetAuthorizeds: FormControl<UsersFormRawValue['budgetAuthorizeds']>;
};

export type UsersFormGroup = FormGroup<UsersFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UsersFormService {
  createUsersFormGroup(users: UsersFormGroupInput = { id: null }): UsersFormGroup {
    const usersRawValue = this.convertUsersToUsersRawValue({
      ...this.getFormDefaults(),
      ...users,
    });
    return new FormGroup<UsersFormGroupContent>({
      id: new FormControl(
        { value: usersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(usersRawValue.name, {
        validators: [Validators.required],
      }),
      email: new FormControl(usersRawValue.email, {
        validators: [Validators.required],
      }),
      password: new FormControl(usersRawValue.password, {
        validators: [Validators.required],
      }),
      active: new FormControl(usersRawValue.active, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(usersRawValue.createdBy),
      createdDate: new FormControl(usersRawValue.createdDate),
      lastModifiedBy: new FormControl(usersRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(usersRawValue.lastModifiedDate),
      userRoles: new FormControl(usersRawValue.userRoles ?? []),
      budgetAuthorizeds: new FormControl(usersRawValue.budgetAuthorizeds ?? []),
    });
  }

  getUsers(form: UsersFormGroup): IUsers | NewUsers {
    return this.convertUsersRawValueToUsers(form.getRawValue() as UsersFormRawValue | NewUsersFormRawValue);
  }

  resetForm(form: UsersFormGroup, users: UsersFormGroupInput): void {
    const usersRawValue = this.convertUsersToUsersRawValue({ ...this.getFormDefaults(), ...users });
    form.reset(
      {
        ...usersRawValue,
        id: { value: usersRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UsersFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
      userRoles: [],
      budgetAuthorizeds: [],
    };
  }

  private convertUsersRawValueToUsers(rawUsers: UsersFormRawValue | NewUsersFormRawValue): IUsers | NewUsers {
    return {
      ...rawUsers,
      createdDate: dayjs(rawUsers.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawUsers.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertUsersToUsersRawValue(
    users: IUsers | (Partial<NewUsers> & UsersFormDefaults),
  ): UsersFormRawValue | PartialWithRequiredKeyOf<NewUsersFormRawValue> {
    return {
      ...users,
      createdDate: users.createdDate ? users.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: users.lastModifiedDate ? users.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      userRoles: users.userRoles ?? [],
      budgetAuthorizeds: users.budgetAuthorizeds ?? [],
    };
  }
}
