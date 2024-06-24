import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../budget.test-samples';

import { BudgetFormService } from './budget-form.service';

describe('Budget Form Service', () => {
  let service: BudgetFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BudgetFormService);
  });

  describe('Service methods', () => {
    describe('createBudgetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBudgetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            estimatedDurationDays: expect.any(Object),
            durationMonths: expect.any(Object),
            monthlyHours: expect.any(Object),
            plannedHours: expect.any(Object),
            resourceCount: expect.any(Object),
            income: expect.any(Object),
            otherTaxes: expect.any(Object),
            descriptionOtherTaxes: expect.any(Object),
            withholdingTaxes: expect.any(Object),
            modAndCifCosts: expect.any(Object),
            grossProfit: expect.any(Object),
            grossProfitPercentage: expect.any(Object),
            grossProfitRule: expect.any(Object),
            absorbedFixedCosts: expect.any(Object),
            otherExpenses: expect.any(Object),
            profitBeforeTax: expect.any(Object),
            estimatedTaxes: expect.any(Object),
            estimatedNetProfit: expect.any(Object),
            netMarginPercentage: expect.any(Object),
            netMarginRule: expect.any(Object),
            commissionToReceive: expect.any(Object),
            needsApproval: expect.any(Object),
            approvalDecision: expect.any(Object),
            approvalDate: expect.any(Object),
            approvalTime: expect.any(Object),
            approvalComments: expect.any(Object),
            approvalStatus: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            contry: expect.any(Object),
            userAssignedTo: expect.any(Object),
            userApprovedBy: expect.any(Object),
            userOwner: expect.any(Object),
            authorizeds: expect.any(Object),
            roleAuthorizeds: expect.any(Object),
          }),
        );
      });

      it('passing IBudget should create a new form with FormGroup', () => {
        const formGroup = service.createBudgetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            estimatedDurationDays: expect.any(Object),
            durationMonths: expect.any(Object),
            monthlyHours: expect.any(Object),
            plannedHours: expect.any(Object),
            resourceCount: expect.any(Object),
            income: expect.any(Object),
            otherTaxes: expect.any(Object),
            descriptionOtherTaxes: expect.any(Object),
            withholdingTaxes: expect.any(Object),
            modAndCifCosts: expect.any(Object),
            grossProfit: expect.any(Object),
            grossProfitPercentage: expect.any(Object),
            grossProfitRule: expect.any(Object),
            absorbedFixedCosts: expect.any(Object),
            otherExpenses: expect.any(Object),
            profitBeforeTax: expect.any(Object),
            estimatedTaxes: expect.any(Object),
            estimatedNetProfit: expect.any(Object),
            netMarginPercentage: expect.any(Object),
            netMarginRule: expect.any(Object),
            commissionToReceive: expect.any(Object),
            needsApproval: expect.any(Object),
            approvalDecision: expect.any(Object),
            approvalDate: expect.any(Object),
            approvalTime: expect.any(Object),
            approvalComments: expect.any(Object),
            approvalStatus: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            contry: expect.any(Object),
            userAssignedTo: expect.any(Object),
            userApprovedBy: expect.any(Object),
            userOwner: expect.any(Object),
            authorizeds: expect.any(Object),
            roleAuthorizeds: expect.any(Object),
          }),
        );
      });
    });

    describe('getBudget', () => {
      it('should return NewBudget for default Budget initial value', () => {
        const formGroup = service.createBudgetFormGroup(sampleWithNewData);

        const budget = service.getBudget(formGroup) as any;

        expect(budget).toMatchObject(sampleWithNewData);
      });

      it('should return NewBudget for empty Budget initial value', () => {
        const formGroup = service.createBudgetFormGroup();

        const budget = service.getBudget(formGroup) as any;

        expect(budget).toMatchObject({});
      });

      it('should return IBudget', () => {
        const formGroup = service.createBudgetFormGroup(sampleWithRequiredData);

        const budget = service.getBudget(formGroup) as any;

        expect(budget).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBudget should not enable id FormControl', () => {
        const formGroup = service.createBudgetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBudget should disable id FormControl', () => {
        const formGroup = service.createBudgetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
