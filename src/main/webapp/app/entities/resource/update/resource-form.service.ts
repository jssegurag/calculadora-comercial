import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IResource, NewResource } from '../resource.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResource for edit and NewResourceFormGroupInput for create.
 */
type ResourceFormGroupInput = IResource | PartialWithRequiredKeyOf<NewResource>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IResource | NewResource> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ResourceFormRawValue = FormValueOf<IResource>;

type NewResourceFormRawValue = FormValueOf<NewResource>;

type ResourceFormDefaults = Pick<NewResource, 'id' | 'active' | 'createdDate' | 'lastModifiedDate'>;

type ResourceFormGroupContent = {
  id: FormControl<ResourceFormRawValue['id'] | NewResource['id']>;
  salary: FormControl<ResourceFormRawValue['salary']>;
  hourlyRate: FormControl<ResourceFormRawValue['hourlyRate']>;
  active: FormControl<ResourceFormRawValue['active']>;
  createdBy: FormControl<ResourceFormRawValue['createdBy']>;
  createdDate: FormControl<ResourceFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ResourceFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ResourceFormRawValue['lastModifiedDate']>;
  position: FormControl<ResourceFormRawValue['position']>;
};

export type ResourceFormGroup = FormGroup<ResourceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResourceFormService {
  createResourceFormGroup(resource: ResourceFormGroupInput = { id: null }): ResourceFormGroup {
    const resourceRawValue = this.convertResourceToResourceRawValue({
      ...this.getFormDefaults(),
      ...resource,
    });
    return new FormGroup<ResourceFormGroupContent>({
      id: new FormControl(
        { value: resourceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      salary: new FormControl(resourceRawValue.salary, {
        validators: [Validators.required],
      }),
      hourlyRate: new FormControl(resourceRawValue.hourlyRate),
      active: new FormControl(resourceRawValue.active, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(resourceRawValue.createdBy),
      createdDate: new FormControl(resourceRawValue.createdDate),
      lastModifiedBy: new FormControl(resourceRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(resourceRawValue.lastModifiedDate),
      position: new FormControl(resourceRawValue.position),
    });
  }

  getResource(form: ResourceFormGroup): IResource | NewResource {
    return this.convertResourceRawValueToResource(form.getRawValue() as ResourceFormRawValue | NewResourceFormRawValue);
  }

  resetForm(form: ResourceFormGroup, resource: ResourceFormGroupInput): void {
    const resourceRawValue = this.convertResourceToResourceRawValue({ ...this.getFormDefaults(), ...resource });
    form.reset(
      {
        ...resourceRawValue,
        id: { value: resourceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResourceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertResourceRawValueToResource(rawResource: ResourceFormRawValue | NewResourceFormRawValue): IResource | NewResource {
    return {
      ...rawResource,
      createdDate: dayjs(rawResource.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawResource.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertResourceToResourceRawValue(
    resource: IResource | (Partial<NewResource> & ResourceFormDefaults),
  ): ResourceFormRawValue | PartialWithRequiredKeyOf<NewResourceFormRawValue> {
    return {
      ...resource,
      createdDate: resource.createdDate ? resource.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: resource.lastModifiedDate ? resource.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
