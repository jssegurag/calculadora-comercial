import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBudgetTemplate, NewBudgetTemplate } from '../budget-template.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBudgetTemplate for edit and NewBudgetTemplateFormGroupInput for create.
 */
type BudgetTemplateFormGroupInput = IBudgetTemplate | PartialWithRequiredKeyOf<NewBudgetTemplate>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBudgetTemplate | NewBudgetTemplate> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BudgetTemplateFormRawValue = FormValueOf<IBudgetTemplate>;

type NewBudgetTemplateFormRawValue = FormValueOf<NewBudgetTemplate>;

type BudgetTemplateFormDefaults = Pick<NewBudgetTemplate, 'id' | 'active' | 'createdDate' | 'lastModifiedDate'>;

type BudgetTemplateFormGroupContent = {
  id: FormControl<BudgetTemplateFormRawValue['id'] | NewBudgetTemplate['id']>;
  name: FormControl<BudgetTemplateFormRawValue['name']>;
  description: FormControl<BudgetTemplateFormRawValue['description']>;
  startDate: FormControl<BudgetTemplateFormRawValue['startDate']>;
  endDate: FormControl<BudgetTemplateFormRawValue['endDate']>;
  estimatedDurationDays: FormControl<BudgetTemplateFormRawValue['estimatedDurationDays']>;
  durationMonths: FormControl<BudgetTemplateFormRawValue['durationMonths']>;
  monthlyHours: FormControl<BudgetTemplateFormRawValue['monthlyHours']>;
  plannedHours: FormControl<BudgetTemplateFormRawValue['plannedHours']>;
  resourceCount: FormControl<BudgetTemplateFormRawValue['resourceCount']>;
  income: FormControl<BudgetTemplateFormRawValue['income']>;
  otherTaxes: FormControl<BudgetTemplateFormRawValue['otherTaxes']>;
  descriptionOtherTaxes: FormControl<BudgetTemplateFormRawValue['descriptionOtherTaxes']>;
  withholdingTaxes: FormControl<BudgetTemplateFormRawValue['withholdingTaxes']>;
  modAndCifCosts: FormControl<BudgetTemplateFormRawValue['modAndCifCosts']>;
  grossProfit: FormControl<BudgetTemplateFormRawValue['grossProfit']>;
  grossProfitPercentage: FormControl<BudgetTemplateFormRawValue['grossProfitPercentage']>;
  grossProfitRule: FormControl<BudgetTemplateFormRawValue['grossProfitRule']>;
  absorbedFixedCosts: FormControl<BudgetTemplateFormRawValue['absorbedFixedCosts']>;
  otherExpenses: FormControl<BudgetTemplateFormRawValue['otherExpenses']>;
  profitBeforeTax: FormControl<BudgetTemplateFormRawValue['profitBeforeTax']>;
  estimatedTaxes: FormControl<BudgetTemplateFormRawValue['estimatedTaxes']>;
  estimatedNetProfit: FormControl<BudgetTemplateFormRawValue['estimatedNetProfit']>;
  netMarginPercentage: FormControl<BudgetTemplateFormRawValue['netMarginPercentage']>;
  netMarginRule: FormControl<BudgetTemplateFormRawValue['netMarginRule']>;
  commissionToReceive: FormControl<BudgetTemplateFormRawValue['commissionToReceive']>;
  active: FormControl<BudgetTemplateFormRawValue['active']>;
  createdBy: FormControl<BudgetTemplateFormRawValue['createdBy']>;
  createdDate: FormControl<BudgetTemplateFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BudgetTemplateFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BudgetTemplateFormRawValue['lastModifiedDate']>;
  country: FormControl<BudgetTemplateFormRawValue['country']>;
};

export type BudgetTemplateFormGroup = FormGroup<BudgetTemplateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BudgetTemplateFormService {
  createBudgetTemplateFormGroup(budgetTemplate: BudgetTemplateFormGroupInput = { id: null }): BudgetTemplateFormGroup {
    const budgetTemplateRawValue = this.convertBudgetTemplateToBudgetTemplateRawValue({
      ...this.getFormDefaults(),
      ...budgetTemplate,
    });
    return new FormGroup<BudgetTemplateFormGroupContent>({
      id: new FormControl(
        { value: budgetTemplateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(budgetTemplateRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(budgetTemplateRawValue.description),
      startDate: new FormControl(budgetTemplateRawValue.startDate),
      endDate: new FormControl(budgetTemplateRawValue.endDate),
      estimatedDurationDays: new FormControl(budgetTemplateRawValue.estimatedDurationDays),
      durationMonths: new FormControl(budgetTemplateRawValue.durationMonths),
      monthlyHours: new FormControl(budgetTemplateRawValue.monthlyHours),
      plannedHours: new FormControl(budgetTemplateRawValue.plannedHours),
      resourceCount: new FormControl(budgetTemplateRawValue.resourceCount),
      income: new FormControl(budgetTemplateRawValue.income),
      otherTaxes: new FormControl(budgetTemplateRawValue.otherTaxes),
      descriptionOtherTaxes: new FormControl(budgetTemplateRawValue.descriptionOtherTaxes),
      withholdingTaxes: new FormControl(budgetTemplateRawValue.withholdingTaxes),
      modAndCifCosts: new FormControl(budgetTemplateRawValue.modAndCifCosts),
      grossProfit: new FormControl(budgetTemplateRawValue.grossProfit),
      grossProfitPercentage: new FormControl(budgetTemplateRawValue.grossProfitPercentage),
      grossProfitRule: new FormControl(budgetTemplateRawValue.grossProfitRule),
      absorbedFixedCosts: new FormControl(budgetTemplateRawValue.absorbedFixedCosts),
      otherExpenses: new FormControl(budgetTemplateRawValue.otherExpenses),
      profitBeforeTax: new FormControl(budgetTemplateRawValue.profitBeforeTax),
      estimatedTaxes: new FormControl(budgetTemplateRawValue.estimatedTaxes),
      estimatedNetProfit: new FormControl(budgetTemplateRawValue.estimatedNetProfit),
      netMarginPercentage: new FormControl(budgetTemplateRawValue.netMarginPercentage),
      netMarginRule: new FormControl(budgetTemplateRawValue.netMarginRule),
      commissionToReceive: new FormControl(budgetTemplateRawValue.commissionToReceive),
      active: new FormControl(budgetTemplateRawValue.active, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(budgetTemplateRawValue.createdBy),
      createdDate: new FormControl(budgetTemplateRawValue.createdDate),
      lastModifiedBy: new FormControl(budgetTemplateRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(budgetTemplateRawValue.lastModifiedDate),
      country: new FormControl(budgetTemplateRawValue.country),
    });
  }

  getBudgetTemplate(form: BudgetTemplateFormGroup): IBudgetTemplate | NewBudgetTemplate {
    return this.convertBudgetTemplateRawValueToBudgetTemplate(
      form.getRawValue() as BudgetTemplateFormRawValue | NewBudgetTemplateFormRawValue,
    );
  }

  resetForm(form: BudgetTemplateFormGroup, budgetTemplate: BudgetTemplateFormGroupInput): void {
    const budgetTemplateRawValue = this.convertBudgetTemplateToBudgetTemplateRawValue({ ...this.getFormDefaults(), ...budgetTemplate });
    form.reset(
      {
        ...budgetTemplateRawValue,
        id: { value: budgetTemplateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BudgetTemplateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBudgetTemplateRawValueToBudgetTemplate(
    rawBudgetTemplate: BudgetTemplateFormRawValue | NewBudgetTemplateFormRawValue,
  ): IBudgetTemplate | NewBudgetTemplate {
    return {
      ...rawBudgetTemplate,
      createdDate: dayjs(rawBudgetTemplate.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBudgetTemplate.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBudgetTemplateToBudgetTemplateRawValue(
    budgetTemplate: IBudgetTemplate | (Partial<NewBudgetTemplate> & BudgetTemplateFormDefaults),
  ): BudgetTemplateFormRawValue | PartialWithRequiredKeyOf<NewBudgetTemplateFormRawValue> {
    return {
      ...budgetTemplate,
      createdDate: budgetTemplate.createdDate ? budgetTemplate.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: budgetTemplate.lastModifiedDate ? budgetTemplate.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
