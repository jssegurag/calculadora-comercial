import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../resource-allocation.test-samples';

import { ResourceAllocationFormService } from './resource-allocation-form.service';

describe('ResourceAllocation Form Service', () => {
  let service: ResourceAllocationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResourceAllocationFormService);
  });

  describe('Service methods', () => {
    describe('createResourceAllocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResourceAllocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            assignedHours: expect.any(Object),
            totalCost: expect.any(Object),
            units: expect.any(Object),
            capacity: expect.any(Object),
            plannedHours: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            budget: expect.any(Object),
            resource: expect.any(Object),
            budgetTemplate: expect.any(Object),
          }),
        );
      });

      it('passing IResourceAllocation should create a new form with FormGroup', () => {
        const formGroup = service.createResourceAllocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            assignedHours: expect.any(Object),
            totalCost: expect.any(Object),
            units: expect.any(Object),
            capacity: expect.any(Object),
            plannedHours: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            budget: expect.any(Object),
            resource: expect.any(Object),
            budgetTemplate: expect.any(Object),
          }),
        );
      });
    });

    describe('getResourceAllocation', () => {
      it('should return NewResourceAllocation for default ResourceAllocation initial value', () => {
        const formGroup = service.createResourceAllocationFormGroup(sampleWithNewData);

        const resourceAllocation = service.getResourceAllocation(formGroup) as any;

        expect(resourceAllocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewResourceAllocation for empty ResourceAllocation initial value', () => {
        const formGroup = service.createResourceAllocationFormGroup();

        const resourceAllocation = service.getResourceAllocation(formGroup) as any;

        expect(resourceAllocation).toMatchObject({});
      });

      it('should return IResourceAllocation', () => {
        const formGroup = service.createResourceAllocationFormGroup(sampleWithRequiredData);

        const resourceAllocation = service.getResourceAllocation(formGroup) as any;

        expect(resourceAllocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResourceAllocation should not enable id FormControl', () => {
        const formGroup = service.createResourceAllocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResourceAllocation should disable id FormControl', () => {
        const formGroup = service.createResourceAllocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
