import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../budget-comment.test-samples';

import { BudgetCommentFormService } from './budget-comment-form.service';

describe('BudgetComment Form Service', () => {
  let service: BudgetCommentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BudgetCommentFormService);
  });

  describe('Service methods', () => {
    describe('createBudgetCommentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBudgetCommentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            content: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            budget: expect.any(Object),
          }),
        );
      });

      it('passing IBudgetComment should create a new form with FormGroup', () => {
        const formGroup = service.createBudgetCommentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            content: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            budget: expect.any(Object),
          }),
        );
      });
    });

    describe('getBudgetComment', () => {
      it('should return NewBudgetComment for default BudgetComment initial value', () => {
        const formGroup = service.createBudgetCommentFormGroup(sampleWithNewData);

        const budgetComment = service.getBudgetComment(formGroup) as any;

        expect(budgetComment).toMatchObject(sampleWithNewData);
      });

      it('should return NewBudgetComment for empty BudgetComment initial value', () => {
        const formGroup = service.createBudgetCommentFormGroup();

        const budgetComment = service.getBudgetComment(formGroup) as any;

        expect(budgetComment).toMatchObject({});
      });

      it('should return IBudgetComment', () => {
        const formGroup = service.createBudgetCommentFormGroup(sampleWithRequiredData);

        const budgetComment = service.getBudgetComment(formGroup) as any;

        expect(budgetComment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBudgetComment should not enable id FormControl', () => {
        const formGroup = service.createBudgetCommentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBudgetComment should disable id FormControl', () => {
        const formGroup = service.createBudgetCommentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
