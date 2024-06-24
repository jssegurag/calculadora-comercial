import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rule-assignment.test-samples';

import { RuleAssignmentFormService } from './rule-assignment-form.service';

describe('RuleAssignment Form Service', () => {
  let service: RuleAssignmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RuleAssignmentFormService);
  });

  describe('Service methods', () => {
    describe('createRuleAssignmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRuleAssignmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            entityName: expect.any(Object),
            entityId: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            droolsRuleFile: expect.any(Object),
          }),
        );
      });

      it('passing IRuleAssignment should create a new form with FormGroup', () => {
        const formGroup = service.createRuleAssignmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            entityName: expect.any(Object),
            entityId: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            droolsRuleFile: expect.any(Object),
          }),
        );
      });
    });

    describe('getRuleAssignment', () => {
      it('should return NewRuleAssignment for default RuleAssignment initial value', () => {
        const formGroup = service.createRuleAssignmentFormGroup(sampleWithNewData);

        const ruleAssignment = service.getRuleAssignment(formGroup) as any;

        expect(ruleAssignment).toMatchObject(sampleWithNewData);
      });

      it('should return NewRuleAssignment for empty RuleAssignment initial value', () => {
        const formGroup = service.createRuleAssignmentFormGroup();

        const ruleAssignment = service.getRuleAssignment(formGroup) as any;

        expect(ruleAssignment).toMatchObject({});
      });

      it('should return IRuleAssignment', () => {
        const formGroup = service.createRuleAssignmentFormGroup(sampleWithRequiredData);

        const ruleAssignment = service.getRuleAssignment(formGroup) as any;

        expect(ruleAssignment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRuleAssignment should not enable id FormControl', () => {
        const formGroup = service.createRuleAssignmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRuleAssignment should disable id FormControl', () => {
        const formGroup = service.createRuleAssignmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
