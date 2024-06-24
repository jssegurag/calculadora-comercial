import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBudget, NewBudget } from '../budget.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBudget for edit and NewBudgetFormGroupInput for create.
 */
type BudgetFormGroupInput = IBudget | PartialWithRequiredKeyOf<NewBudget>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBudget | NewBudget> = Omit<T, 'approvalDate' | 'approvalTime' | 'createdDate' | 'lastModifiedDate'> & {
  approvalDate?: string | null;
  approvalTime?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BudgetFormRawValue = FormValueOf<IBudget>;

type NewBudgetFormRawValue = FormValueOf<NewBudget>;

type BudgetFormDefaults = Pick<
  NewBudget,
  'id' | 'needsApproval' | 'approvalDate' | 'approvalTime' | 'createdDate' | 'lastModifiedDate' | 'authorizeds' | 'roleAuthorizeds'
>;

type BudgetFormGroupContent = {
  id: FormControl<BudgetFormRawValue['id'] | NewBudget['id']>;
  name: FormControl<BudgetFormRawValue['name']>;
  description: FormControl<BudgetFormRawValue['description']>;
  startDate: FormControl<BudgetFormRawValue['startDate']>;
  endDate: FormControl<BudgetFormRawValue['endDate']>;
  estimatedDurationDays: FormControl<BudgetFormRawValue['estimatedDurationDays']>;
  durationMonths: FormControl<BudgetFormRawValue['durationMonths']>;
  monthlyHours: FormControl<BudgetFormRawValue['monthlyHours']>;
  plannedHours: FormControl<BudgetFormRawValue['plannedHours']>;
  resourceCount: FormControl<BudgetFormRawValue['resourceCount']>;
  income: FormControl<BudgetFormRawValue['income']>;
  otherTaxes: FormControl<BudgetFormRawValue['otherTaxes']>;
  descriptionOtherTaxes: FormControl<BudgetFormRawValue['descriptionOtherTaxes']>;
  withholdingTaxes: FormControl<BudgetFormRawValue['withholdingTaxes']>;
  modAndCifCosts: FormControl<BudgetFormRawValue['modAndCifCosts']>;
  grossProfit: FormControl<BudgetFormRawValue['grossProfit']>;
  grossProfitPercentage: FormControl<BudgetFormRawValue['grossProfitPercentage']>;
  grossProfitRule: FormControl<BudgetFormRawValue['grossProfitRule']>;
  absorbedFixedCosts: FormControl<BudgetFormRawValue['absorbedFixedCosts']>;
  otherExpenses: FormControl<BudgetFormRawValue['otherExpenses']>;
  profitBeforeTax: FormControl<BudgetFormRawValue['profitBeforeTax']>;
  estimatedTaxes: FormControl<BudgetFormRawValue['estimatedTaxes']>;
  estimatedNetProfit: FormControl<BudgetFormRawValue['estimatedNetProfit']>;
  netMarginPercentage: FormControl<BudgetFormRawValue['netMarginPercentage']>;
  netMarginRule: FormControl<BudgetFormRawValue['netMarginRule']>;
  commissionToReceive: FormControl<BudgetFormRawValue['commissionToReceive']>;
  needsApproval: FormControl<BudgetFormRawValue['needsApproval']>;
  approvalDecision: FormControl<BudgetFormRawValue['approvalDecision']>;
  approvalDate: FormControl<BudgetFormRawValue['approvalDate']>;
  approvalTime: FormControl<BudgetFormRawValue['approvalTime']>;
  approvalComments: FormControl<BudgetFormRawValue['approvalComments']>;
  approvalStatus: FormControl<BudgetFormRawValue['approvalStatus']>;
  createdBy: FormControl<BudgetFormRawValue['createdBy']>;
  createdDate: FormControl<BudgetFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BudgetFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BudgetFormRawValue['lastModifiedDate']>;
  contry: FormControl<BudgetFormRawValue['contry']>;
  userAssignedTo: FormControl<BudgetFormRawValue['userAssignedTo']>;
  userApprovedBy: FormControl<BudgetFormRawValue['userApprovedBy']>;
  userOwner: FormControl<BudgetFormRawValue['userOwner']>;
  authorizeds: FormControl<BudgetFormRawValue['authorizeds']>;
  roleAuthorizeds: FormControl<BudgetFormRawValue['roleAuthorizeds']>;
};

export type BudgetFormGroup = FormGroup<BudgetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BudgetFormService {
  createBudgetFormGroup(budget: BudgetFormGroupInput = { id: null }): BudgetFormGroup {
    const budgetRawValue = this.convertBudgetToBudgetRawValue({
      ...this.getFormDefaults(),
      ...budget,
    });
    return new FormGroup<BudgetFormGroupContent>({
      id: new FormControl(
        { value: budgetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(budgetRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(budgetRawValue.description),
      startDate: new FormControl(budgetRawValue.startDate),
      endDate: new FormControl(budgetRawValue.endDate),
      estimatedDurationDays: new FormControl(budgetRawValue.estimatedDurationDays),
      durationMonths: new FormControl(budgetRawValue.durationMonths),
      monthlyHours: new FormControl(budgetRawValue.monthlyHours),
      plannedHours: new FormControl(budgetRawValue.plannedHours),
      resourceCount: new FormControl(budgetRawValue.resourceCount),
      income: new FormControl(budgetRawValue.income),
      otherTaxes: new FormControl(budgetRawValue.otherTaxes),
      descriptionOtherTaxes: new FormControl(budgetRawValue.descriptionOtherTaxes),
      withholdingTaxes: new FormControl(budgetRawValue.withholdingTaxes),
      modAndCifCosts: new FormControl(budgetRawValue.modAndCifCosts),
      grossProfit: new FormControl(budgetRawValue.grossProfit),
      grossProfitPercentage: new FormControl(budgetRawValue.grossProfitPercentage),
      grossProfitRule: new FormControl(budgetRawValue.grossProfitRule),
      absorbedFixedCosts: new FormControl(budgetRawValue.absorbedFixedCosts),
      otherExpenses: new FormControl(budgetRawValue.otherExpenses),
      profitBeforeTax: new FormControl(budgetRawValue.profitBeforeTax),
      estimatedTaxes: new FormControl(budgetRawValue.estimatedTaxes),
      estimatedNetProfit: new FormControl(budgetRawValue.estimatedNetProfit),
      netMarginPercentage: new FormControl(budgetRawValue.netMarginPercentage),
      netMarginRule: new FormControl(budgetRawValue.netMarginRule),
      commissionToReceive: new FormControl(budgetRawValue.commissionToReceive),
      needsApproval: new FormControl(budgetRawValue.needsApproval),
      approvalDecision: new FormControl(budgetRawValue.approvalDecision),
      approvalDate: new FormControl(budgetRawValue.approvalDate),
      approvalTime: new FormControl(budgetRawValue.approvalTime),
      approvalComments: new FormControl(budgetRawValue.approvalComments),
      approvalStatus: new FormControl(budgetRawValue.approvalStatus),
      createdBy: new FormControl(budgetRawValue.createdBy),
      createdDate: new FormControl(budgetRawValue.createdDate),
      lastModifiedBy: new FormControl(budgetRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(budgetRawValue.lastModifiedDate),
      contry: new FormControl(budgetRawValue.contry),
      userAssignedTo: new FormControl(budgetRawValue.userAssignedTo),
      userApprovedBy: new FormControl(budgetRawValue.userApprovedBy),
      userOwner: new FormControl(budgetRawValue.userOwner),
      authorizeds: new FormControl(budgetRawValue.authorizeds ?? []),
      roleAuthorizeds: new FormControl(budgetRawValue.roleAuthorizeds ?? []),
    });
  }

  getBudget(form: BudgetFormGroup): IBudget | NewBudget {
    return this.convertBudgetRawValueToBudget(form.getRawValue() as BudgetFormRawValue | NewBudgetFormRawValue);
  }

  resetForm(form: BudgetFormGroup, budget: BudgetFormGroupInput): void {
    const budgetRawValue = this.convertBudgetToBudgetRawValue({ ...this.getFormDefaults(), ...budget });
    form.reset(
      {
        ...budgetRawValue,
        id: { value: budgetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BudgetFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      needsApproval: false,
      approvalDate: currentTime,
      approvalTime: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
      authorizeds: [],
      roleAuthorizeds: [],
    };
  }

  private convertBudgetRawValueToBudget(rawBudget: BudgetFormRawValue | NewBudgetFormRawValue): IBudget | NewBudget {
    return {
      ...rawBudget,
      approvalDate: dayjs(rawBudget.approvalDate, DATE_TIME_FORMAT),
      approvalTime: dayjs(rawBudget.approvalTime, DATE_TIME_FORMAT),
      createdDate: dayjs(rawBudget.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBudget.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBudgetToBudgetRawValue(
    budget: IBudget | (Partial<NewBudget> & BudgetFormDefaults),
  ): BudgetFormRawValue | PartialWithRequiredKeyOf<NewBudgetFormRawValue> {
    return {
      ...budget,
      approvalDate: budget.approvalDate ? budget.approvalDate.format(DATE_TIME_FORMAT) : undefined,
      approvalTime: budget.approvalTime ? budget.approvalTime.format(DATE_TIME_FORMAT) : undefined,
      createdDate: budget.createdDate ? budget.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: budget.lastModifiedDate ? budget.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      authorizeds: budget.authorizeds ?? [],
      roleAuthorizeds: budget.roleAuthorizeds ?? [],
    };
  }
}
