import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResourceAllocation } from '../resource-allocation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../resource-allocation.test-samples';

import { ResourceAllocationService, RestResourceAllocation } from './resource-allocation.service';

const requireRestSample: RestResourceAllocation = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ResourceAllocation Service', () => {
  let service: ResourceAllocationService;
  let httpMock: HttpTestingController;
  let expectedResult: IResourceAllocation | IResourceAllocation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ResourceAllocationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ResourceAllocation', () => {
      const resourceAllocation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(resourceAllocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ResourceAllocation', () => {
      const resourceAllocation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(resourceAllocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ResourceAllocation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ResourceAllocation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ResourceAllocation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addResourceAllocationToCollectionIfMissing', () => {
      it('should add a ResourceAllocation to an empty array', () => {
        const resourceAllocation: IResourceAllocation = sampleWithRequiredData;
        expectedResult = service.addResourceAllocationToCollectionIfMissing([], resourceAllocation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resourceAllocation);
      });

      it('should not add a ResourceAllocation to an array that contains it', () => {
        const resourceAllocation: IResourceAllocation = sampleWithRequiredData;
        const resourceAllocationCollection: IResourceAllocation[] = [
          {
            ...resourceAllocation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addResourceAllocationToCollectionIfMissing(resourceAllocationCollection, resourceAllocation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ResourceAllocation to an array that doesn't contain it", () => {
        const resourceAllocation: IResourceAllocation = sampleWithRequiredData;
        const resourceAllocationCollection: IResourceAllocation[] = [sampleWithPartialData];
        expectedResult = service.addResourceAllocationToCollectionIfMissing(resourceAllocationCollection, resourceAllocation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resourceAllocation);
      });

      it('should add only unique ResourceAllocation to an array', () => {
        const resourceAllocationArray: IResourceAllocation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const resourceAllocationCollection: IResourceAllocation[] = [sampleWithRequiredData];
        expectedResult = service.addResourceAllocationToCollectionIfMissing(resourceAllocationCollection, ...resourceAllocationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resourceAllocation: IResourceAllocation = sampleWithRequiredData;
        const resourceAllocation2: IResourceAllocation = sampleWithPartialData;
        expectedResult = service.addResourceAllocationToCollectionIfMissing([], resourceAllocation, resourceAllocation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resourceAllocation);
        expect(expectedResult).toContain(resourceAllocation2);
      });

      it('should accept null and undefined values', () => {
        const resourceAllocation: IResourceAllocation = sampleWithRequiredData;
        expectedResult = service.addResourceAllocationToCollectionIfMissing([], null, resourceAllocation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resourceAllocation);
      });

      it('should return initial array if no ResourceAllocation is added', () => {
        const resourceAllocationCollection: IResourceAllocation[] = [sampleWithRequiredData];
        expectedResult = service.addResourceAllocationToCollectionIfMissing(resourceAllocationCollection, undefined, null);
        expect(expectedResult).toEqual(resourceAllocationCollection);
      });
    });

    describe('compareResourceAllocation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareResourceAllocation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareResourceAllocation(entity1, entity2);
        const compareResult2 = service.compareResourceAllocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareResourceAllocation(entity1, entity2);
        const compareResult2 = service.compareResourceAllocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareResourceAllocation(entity1, entity2);
        const compareResult2 = service.compareResourceAllocation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
