import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IResourceAllocation, NewResourceAllocation } from '../resource-allocation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResourceAllocation for edit and NewResourceAllocationFormGroupInput for create.
 */
type ResourceAllocationFormGroupInput = IResourceAllocation | PartialWithRequiredKeyOf<NewResourceAllocation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IResourceAllocation | NewResourceAllocation> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ResourceAllocationFormRawValue = FormValueOf<IResourceAllocation>;

type NewResourceAllocationFormRawValue = FormValueOf<NewResourceAllocation>;

type ResourceAllocationFormDefaults = Pick<NewResourceAllocation, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ResourceAllocationFormGroupContent = {
  id: FormControl<ResourceAllocationFormRawValue['id'] | NewResourceAllocation['id']>;
  assignedHours: FormControl<ResourceAllocationFormRawValue['assignedHours']>;
  totalCost: FormControl<ResourceAllocationFormRawValue['totalCost']>;
  units: FormControl<ResourceAllocationFormRawValue['units']>;
  capacity: FormControl<ResourceAllocationFormRawValue['capacity']>;
  plannedHours: FormControl<ResourceAllocationFormRawValue['plannedHours']>;
  createdBy: FormControl<ResourceAllocationFormRawValue['createdBy']>;
  createdDate: FormControl<ResourceAllocationFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ResourceAllocationFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ResourceAllocationFormRawValue['lastModifiedDate']>;
  budget: FormControl<ResourceAllocationFormRawValue['budget']>;
  resource: FormControl<ResourceAllocationFormRawValue['resource']>;
  budgetTemplate: FormControl<ResourceAllocationFormRawValue['budgetTemplate']>;
};

export type ResourceAllocationFormGroup = FormGroup<ResourceAllocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResourceAllocationFormService {
  createResourceAllocationFormGroup(resourceAllocation: ResourceAllocationFormGroupInput = { id: null }): ResourceAllocationFormGroup {
    const resourceAllocationRawValue = this.convertResourceAllocationToResourceAllocationRawValue({
      ...this.getFormDefaults(),
      ...resourceAllocation,
    });
    return new FormGroup<ResourceAllocationFormGroupContent>({
      id: new FormControl(
        { value: resourceAllocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      assignedHours: new FormControl(resourceAllocationRawValue.assignedHours, {
        validators: [Validators.required],
      }),
      totalCost: new FormControl(resourceAllocationRawValue.totalCost),
      units: new FormControl(resourceAllocationRawValue.units),
      capacity: new FormControl(resourceAllocationRawValue.capacity),
      plannedHours: new FormControl(resourceAllocationRawValue.plannedHours),
      createdBy: new FormControl(resourceAllocationRawValue.createdBy),
      createdDate: new FormControl(resourceAllocationRawValue.createdDate),
      lastModifiedBy: new FormControl(resourceAllocationRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(resourceAllocationRawValue.lastModifiedDate),
      budget: new FormControl(resourceAllocationRawValue.budget),
      resource: new FormControl(resourceAllocationRawValue.resource),
      budgetTemplate: new FormControl(resourceAllocationRawValue.budgetTemplate),
    });
  }

  getResourceAllocation(form: ResourceAllocationFormGroup): IResourceAllocation | NewResourceAllocation {
    return this.convertResourceAllocationRawValueToResourceAllocation(
      form.getRawValue() as ResourceAllocationFormRawValue | NewResourceAllocationFormRawValue,
    );
  }

  resetForm(form: ResourceAllocationFormGroup, resourceAllocation: ResourceAllocationFormGroupInput): void {
    const resourceAllocationRawValue = this.convertResourceAllocationToResourceAllocationRawValue({
      ...this.getFormDefaults(),
      ...resourceAllocation,
    });
    form.reset(
      {
        ...resourceAllocationRawValue,
        id: { value: resourceAllocationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResourceAllocationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertResourceAllocationRawValueToResourceAllocation(
    rawResourceAllocation: ResourceAllocationFormRawValue | NewResourceAllocationFormRawValue,
  ): IResourceAllocation | NewResourceAllocation {
    return {
      ...rawResourceAllocation,
      createdDate: dayjs(rawResourceAllocation.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawResourceAllocation.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertResourceAllocationToResourceAllocationRawValue(
    resourceAllocation: IResourceAllocation | (Partial<NewResourceAllocation> & ResourceAllocationFormDefaults),
  ): ResourceAllocationFormRawValue | PartialWithRequiredKeyOf<NewResourceAllocationFormRawValue> {
    return {
      ...resourceAllocation,
      createdDate: resourceAllocation.createdDate ? resourceAllocation.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: resourceAllocation.lastModifiedDate ? resourceAllocation.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
