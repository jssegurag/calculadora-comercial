import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../financial-parameter.test-samples';

import { FinancialParameterFormService } from './financial-parameter-form.service';

describe('FinancialParameter Form Service', () => {
  let service: FinancialParameterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinancialParameterFormService);
  });

  describe('Service methods', () => {
    describe('createFinancialParameterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFinancialParameterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            active: expect.any(Object),
            mandatory: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            financialParameterType: expect.any(Object),
            country: expect.any(Object),
            administrator: expect.any(Object),
            roleAuthorizeds: expect.any(Object),
          }),
        );
      });

      it('passing IFinancialParameter should create a new form with FormGroup', () => {
        const formGroup = service.createFinancialParameterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            active: expect.any(Object),
            mandatory: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            financialParameterType: expect.any(Object),
            country: expect.any(Object),
            administrator: expect.any(Object),
            roleAuthorizeds: expect.any(Object),
          }),
        );
      });
    });

    describe('getFinancialParameter', () => {
      it('should return NewFinancialParameter for default FinancialParameter initial value', () => {
        const formGroup = service.createFinancialParameterFormGroup(sampleWithNewData);

        const financialParameter = service.getFinancialParameter(formGroup) as any;

        expect(financialParameter).toMatchObject(sampleWithNewData);
      });

      it('should return NewFinancialParameter for empty FinancialParameter initial value', () => {
        const formGroup = service.createFinancialParameterFormGroup();

        const financialParameter = service.getFinancialParameter(formGroup) as any;

        expect(financialParameter).toMatchObject({});
      });

      it('should return IFinancialParameter', () => {
        const formGroup = service.createFinancialParameterFormGroup(sampleWithRequiredData);

        const financialParameter = service.getFinancialParameter(formGroup) as any;

        expect(financialParameter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFinancialParameter should not enable id FormControl', () => {
        const formGroup = service.createFinancialParameterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFinancialParameter should disable id FormControl', () => {
        const formGroup = service.createFinancialParameterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
