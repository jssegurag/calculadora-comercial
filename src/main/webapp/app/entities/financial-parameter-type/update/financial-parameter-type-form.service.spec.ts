import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../financial-parameter-type.test-samples';

import { FinancialParameterTypeFormService } from './financial-parameter-type-form.service';

describe('FinancialParameterType Form Service', () => {
  let service: FinancialParameterTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinancialParameterTypeFormService);
  });

  describe('Service methods', () => {
    describe('createFinancialParameterTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFinancialParameterTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            active: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IFinancialParameterType should create a new form with FormGroup', () => {
        const formGroup = service.createFinancialParameterTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            active: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getFinancialParameterType', () => {
      it('should return NewFinancialParameterType for default FinancialParameterType initial value', () => {
        const formGroup = service.createFinancialParameterTypeFormGroup(sampleWithNewData);

        const financialParameterType = service.getFinancialParameterType(formGroup) as any;

        expect(financialParameterType).toMatchObject(sampleWithNewData);
      });

      it('should return NewFinancialParameterType for empty FinancialParameterType initial value', () => {
        const formGroup = service.createFinancialParameterTypeFormGroup();

        const financialParameterType = service.getFinancialParameterType(formGroup) as any;

        expect(financialParameterType).toMatchObject({});
      });

      it('should return IFinancialParameterType', () => {
        const formGroup = service.createFinancialParameterTypeFormGroup(sampleWithRequiredData);

        const financialParameterType = service.getFinancialParameterType(formGroup) as any;

        expect(financialParameterType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFinancialParameterType should not enable id FormControl', () => {
        const formGroup = service.createFinancialParameterTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFinancialParameterType should disable id FormControl', () => {
        const formGroup = service.createFinancialParameterTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
