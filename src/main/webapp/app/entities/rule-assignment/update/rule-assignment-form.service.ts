import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRuleAssignment, NewRuleAssignment } from '../rule-assignment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRuleAssignment for edit and NewRuleAssignmentFormGroupInput for create.
 */
type RuleAssignmentFormGroupInput = IRuleAssignment | PartialWithRequiredKeyOf<NewRuleAssignment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRuleAssignment | NewRuleAssignment> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type RuleAssignmentFormRawValue = FormValueOf<IRuleAssignment>;

type NewRuleAssignmentFormRawValue = FormValueOf<NewRuleAssignment>;

type RuleAssignmentFormDefaults = Pick<NewRuleAssignment, 'id' | 'createdDate' | 'lastModifiedDate'>;

type RuleAssignmentFormGroupContent = {
  id: FormControl<RuleAssignmentFormRawValue['id'] | NewRuleAssignment['id']>;
  entityName: FormControl<RuleAssignmentFormRawValue['entityName']>;
  entityId: FormControl<RuleAssignmentFormRawValue['entityId']>;
  createdBy: FormControl<RuleAssignmentFormRawValue['createdBy']>;
  createdDate: FormControl<RuleAssignmentFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<RuleAssignmentFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<RuleAssignmentFormRawValue['lastModifiedDate']>;
  droolsRuleFile: FormControl<RuleAssignmentFormRawValue['droolsRuleFile']>;
};

export type RuleAssignmentFormGroup = FormGroup<RuleAssignmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RuleAssignmentFormService {
  createRuleAssignmentFormGroup(ruleAssignment: RuleAssignmentFormGroupInput = { id: null }): RuleAssignmentFormGroup {
    const ruleAssignmentRawValue = this.convertRuleAssignmentToRuleAssignmentRawValue({
      ...this.getFormDefaults(),
      ...ruleAssignment,
    });
    return new FormGroup<RuleAssignmentFormGroupContent>({
      id: new FormControl(
        { value: ruleAssignmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      entityName: new FormControl(ruleAssignmentRawValue.entityName, {
        validators: [Validators.required],
      }),
      entityId: new FormControl(ruleAssignmentRawValue.entityId, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(ruleAssignmentRawValue.createdBy),
      createdDate: new FormControl(ruleAssignmentRawValue.createdDate),
      lastModifiedBy: new FormControl(ruleAssignmentRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(ruleAssignmentRawValue.lastModifiedDate),
      droolsRuleFile: new FormControl(ruleAssignmentRawValue.droolsRuleFile),
    });
  }

  getRuleAssignment(form: RuleAssignmentFormGroup): IRuleAssignment | NewRuleAssignment {
    return this.convertRuleAssignmentRawValueToRuleAssignment(
      form.getRawValue() as RuleAssignmentFormRawValue | NewRuleAssignmentFormRawValue,
    );
  }

  resetForm(form: RuleAssignmentFormGroup, ruleAssignment: RuleAssignmentFormGroupInput): void {
    const ruleAssignmentRawValue = this.convertRuleAssignmentToRuleAssignmentRawValue({ ...this.getFormDefaults(), ...ruleAssignment });
    form.reset(
      {
        ...ruleAssignmentRawValue,
        id: { value: ruleAssignmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RuleAssignmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertRuleAssignmentRawValueToRuleAssignment(
    rawRuleAssignment: RuleAssignmentFormRawValue | NewRuleAssignmentFormRawValue,
  ): IRuleAssignment | NewRuleAssignment {
    return {
      ...rawRuleAssignment,
      createdDate: dayjs(rawRuleAssignment.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawRuleAssignment.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertRuleAssignmentToRuleAssignmentRawValue(
    ruleAssignment: IRuleAssignment | (Partial<NewRuleAssignment> & RuleAssignmentFormDefaults),
  ): RuleAssignmentFormRawValue | PartialWithRequiredKeyOf<NewRuleAssignmentFormRawValue> {
    return {
      ...ruleAssignment,
      createdDate: ruleAssignment.createdDate ? ruleAssignment.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: ruleAssignment.lastModifiedDate ? ruleAssignment.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
