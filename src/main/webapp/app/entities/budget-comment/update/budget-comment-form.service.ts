import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBudgetComment, NewBudgetComment } from '../budget-comment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBudgetComment for edit and NewBudgetCommentFormGroupInput for create.
 */
type BudgetCommentFormGroupInput = IBudgetComment | PartialWithRequiredKeyOf<NewBudgetComment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBudgetComment | NewBudgetComment> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BudgetCommentFormRawValue = FormValueOf<IBudgetComment>;

type NewBudgetCommentFormRawValue = FormValueOf<NewBudgetComment>;

type BudgetCommentFormDefaults = Pick<NewBudgetComment, 'id' | 'createdDate' | 'lastModifiedDate'>;

type BudgetCommentFormGroupContent = {
  id: FormControl<BudgetCommentFormRawValue['id'] | NewBudgetComment['id']>;
  content: FormControl<BudgetCommentFormRawValue['content']>;
  createdBy: FormControl<BudgetCommentFormRawValue['createdBy']>;
  createdDate: FormControl<BudgetCommentFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BudgetCommentFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BudgetCommentFormRawValue['lastModifiedDate']>;
  budget: FormControl<BudgetCommentFormRawValue['budget']>;
};

export type BudgetCommentFormGroup = FormGroup<BudgetCommentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BudgetCommentFormService {
  createBudgetCommentFormGroup(budgetComment: BudgetCommentFormGroupInput = { id: null }): BudgetCommentFormGroup {
    const budgetCommentRawValue = this.convertBudgetCommentToBudgetCommentRawValue({
      ...this.getFormDefaults(),
      ...budgetComment,
    });
    return new FormGroup<BudgetCommentFormGroupContent>({
      id: new FormControl(
        { value: budgetCommentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      content: new FormControl(budgetCommentRawValue.content, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(budgetCommentRawValue.createdBy),
      createdDate: new FormControl(budgetCommentRawValue.createdDate),
      lastModifiedBy: new FormControl(budgetCommentRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(budgetCommentRawValue.lastModifiedDate),
      budget: new FormControl(budgetCommentRawValue.budget),
    });
  }

  getBudgetComment(form: BudgetCommentFormGroup): IBudgetComment | NewBudgetComment {
    return this.convertBudgetCommentRawValueToBudgetComment(form.getRawValue() as BudgetCommentFormRawValue | NewBudgetCommentFormRawValue);
  }

  resetForm(form: BudgetCommentFormGroup, budgetComment: BudgetCommentFormGroupInput): void {
    const budgetCommentRawValue = this.convertBudgetCommentToBudgetCommentRawValue({ ...this.getFormDefaults(), ...budgetComment });
    form.reset(
      {
        ...budgetCommentRawValue,
        id: { value: budgetCommentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BudgetCommentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBudgetCommentRawValueToBudgetComment(
    rawBudgetComment: BudgetCommentFormRawValue | NewBudgetCommentFormRawValue,
  ): IBudgetComment | NewBudgetComment {
    return {
      ...rawBudgetComment,
      createdDate: dayjs(rawBudgetComment.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBudgetComment.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBudgetCommentToBudgetCommentRawValue(
    budgetComment: IBudgetComment | (Partial<NewBudgetComment> & BudgetCommentFormDefaults),
  ): BudgetCommentFormRawValue | PartialWithRequiredKeyOf<NewBudgetCommentFormRawValue> {
    return {
      ...budgetComment,
      createdDate: budgetComment.createdDate ? budgetComment.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: budgetComment.lastModifiedDate ? budgetComment.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
