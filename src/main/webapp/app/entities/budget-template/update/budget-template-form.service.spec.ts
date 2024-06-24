import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../budget-template.test-samples';

import { BudgetTemplateFormService } from './budget-template-form.service';

describe('BudgetTemplate Form Service', () => {
  let service: BudgetTemplateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BudgetTemplateFormService);
  });

  describe('Service methods', () => {
    describe('createBudgetTemplateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBudgetTemplateFormGroup();

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
            active: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            country: expect.any(Object),
          }),
        );
      });

      it('passing IBudgetTemplate should create a new form with FormGroup', () => {
        const formGroup = service.createBudgetTemplateFormGroup(sampleWithRequiredData);

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
            active: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            country: expect.any(Object),
          }),
        );
      });
    });

    describe('getBudgetTemplate', () => {
      it('should return NewBudgetTemplate for default BudgetTemplate initial value', () => {
        const formGroup = service.createBudgetTemplateFormGroup(sampleWithNewData);

        const budgetTemplate = service.getBudgetTemplate(formGroup) as any;

        expect(budgetTemplate).toMatchObject(sampleWithNewData);
      });

      it('should return NewBudgetTemplate for empty BudgetTemplate initial value', () => {
        const formGroup = service.createBudgetTemplateFormGroup();

        const budgetTemplate = service.getBudgetTemplate(formGroup) as any;

        expect(budgetTemplate).toMatchObject({});
      });

      it('should return IBudgetTemplate', () => {
        const formGroup = service.createBudgetTemplateFormGroup(sampleWithRequiredData);

        const budgetTemplate = service.getBudgetTemplate(formGroup) as any;

        expect(budgetTemplate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBudgetTemplate should not enable id FormControl', () => {
        const formGroup = service.createBudgetTemplateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBudgetTemplate should disable id FormControl', () => {
        const formGroup = service.createBudgetTemplateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
