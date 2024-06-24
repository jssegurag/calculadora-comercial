import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICountry, NewCountry } from '../country.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICountry for edit and NewCountryFormGroupInput for create.
 */
type CountryFormGroupInput = ICountry | PartialWithRequiredKeyOf<NewCountry>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICountry | NewCountry> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type CountryFormRawValue = FormValueOf<ICountry>;

type NewCountryFormRawValue = FormValueOf<NewCountry>;

type CountryFormDefaults = Pick<NewCountry, 'id' | 'active' | 'createdDate' | 'lastModifiedDate'>;

type CountryFormGroupContent = {
  id: FormControl<CountryFormRawValue['id'] | NewCountry['id']>;
  name: FormControl<CountryFormRawValue['name']>;
  active: FormControl<CountryFormRawValue['active']>;
  createdBy: FormControl<CountryFormRawValue['createdBy']>;
  createdDate: FormControl<CountryFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<CountryFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<CountryFormRawValue['lastModifiedDate']>;
};

export type CountryFormGroup = FormGroup<CountryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CountryFormService {
  createCountryFormGroup(country: CountryFormGroupInput = { id: null }): CountryFormGroup {
    const countryRawValue = this.convertCountryToCountryRawValue({
      ...this.getFormDefaults(),
      ...country,
    });
    return new FormGroup<CountryFormGroupContent>({
      id: new FormControl(
        { value: countryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(countryRawValue.name, {
        validators: [Validators.required],
      }),
      active: new FormControl(countryRawValue.active, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(countryRawValue.createdBy),
      createdDate: new FormControl(countryRawValue.createdDate),
      lastModifiedBy: new FormControl(countryRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(countryRawValue.lastModifiedDate),
    });
  }

  getCountry(form: CountryFormGroup): ICountry | NewCountry {
    return this.convertCountryRawValueToCountry(form.getRawValue() as CountryFormRawValue | NewCountryFormRawValue);
  }

  resetForm(form: CountryFormGroup, country: CountryFormGroupInput): void {
    const countryRawValue = this.convertCountryToCountryRawValue({ ...this.getFormDefaults(), ...country });
    form.reset(
      {
        ...countryRawValue,
        id: { value: countryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CountryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertCountryRawValueToCountry(rawCountry: CountryFormRawValue | NewCountryFormRawValue): ICountry | NewCountry {
    return {
      ...rawCountry,
      createdDate: dayjs(rawCountry.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawCountry.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCountryToCountryRawValue(
    country: ICountry | (Partial<NewCountry> & CountryFormDefaults),
  ): CountryFormRawValue | PartialWithRequiredKeyOf<NewCountryFormRawValue> {
    return {
      ...country,
      createdDate: country.createdDate ? country.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: country.lastModifiedDate ? country.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
