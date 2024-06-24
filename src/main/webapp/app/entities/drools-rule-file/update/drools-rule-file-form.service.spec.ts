import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../drools-rule-file.test-samples';

import { DroolsRuleFileFormService } from './drools-rule-file-form.service';

describe('DroolsRuleFile Form Service', () => {
  let service: DroolsRuleFileFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DroolsRuleFileFormService);
  });

  describe('Service methods', () => {
    describe('createDroolsRuleFileFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDroolsRuleFileFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fileName: expect.any(Object),
            fileContent: expect.any(Object),
            description: expect.any(Object),
            active: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IDroolsRuleFile should create a new form with FormGroup', () => {
        const formGroup = service.createDroolsRuleFileFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fileName: expect.any(Object),
            fileContent: expect.any(Object),
            description: expect.any(Object),
            active: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getDroolsRuleFile', () => {
      it('should return NewDroolsRuleFile for default DroolsRuleFile initial value', () => {
        const formGroup = service.createDroolsRuleFileFormGroup(sampleWithNewData);

        const droolsRuleFile = service.getDroolsRuleFile(formGroup) as any;

        expect(droolsRuleFile).toMatchObject(sampleWithNewData);
      });

      it('should return NewDroolsRuleFile for empty DroolsRuleFile initial value', () => {
        const formGroup = service.createDroolsRuleFileFormGroup();

        const droolsRuleFile = service.getDroolsRuleFile(formGroup) as any;

        expect(droolsRuleFile).toMatchObject({});
      });

      it('should return IDroolsRuleFile', () => {
        const formGroup = service.createDroolsRuleFileFormGroup(sampleWithRequiredData);

        const droolsRuleFile = service.getDroolsRuleFile(formGroup) as any;

        expect(droolsRuleFile).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDroolsRuleFile should not enable id FormControl', () => {
        const formGroup = service.createDroolsRuleFileFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDroolsRuleFile should disable id FormControl', () => {
        const formGroup = service.createDroolsRuleFileFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
