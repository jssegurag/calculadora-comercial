import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFinancialParameter } from '../financial-parameter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../financial-parameter.test-samples';

import { FinancialParameterService, RestFinancialParameter } from './financial-parameter.service';

const requireRestSample: RestFinancialParameter = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('FinancialParameter Service', () => {
  let service: FinancialParameterService;
  let httpMock: HttpTestingController;
  let expectedResult: IFinancialParameter | IFinancialParameter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FinancialParameterService);
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

    it('should create a FinancialParameter', () => {
      const financialParameter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(financialParameter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FinancialParameter', () => {
      const financialParameter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(financialParameter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FinancialParameter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FinancialParameter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FinancialParameter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFinancialParameterToCollectionIfMissing', () => {
      it('should add a FinancialParameter to an empty array', () => {
        const financialParameter: IFinancialParameter = sampleWithRequiredData;
        expectedResult = service.addFinancialParameterToCollectionIfMissing([], financialParameter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(financialParameter);
      });

      it('should not add a FinancialParameter to an array that contains it', () => {
        const financialParameter: IFinancialParameter = sampleWithRequiredData;
        const financialParameterCollection: IFinancialParameter[] = [
          {
            ...financialParameter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFinancialParameterToCollectionIfMissing(financialParameterCollection, financialParameter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FinancialParameter to an array that doesn't contain it", () => {
        const financialParameter: IFinancialParameter = sampleWithRequiredData;
        const financialParameterCollection: IFinancialParameter[] = [sampleWithPartialData];
        expectedResult = service.addFinancialParameterToCollectionIfMissing(financialParameterCollection, financialParameter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(financialParameter);
      });

      it('should add only unique FinancialParameter to an array', () => {
        const financialParameterArray: IFinancialParameter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const financialParameterCollection: IFinancialParameter[] = [sampleWithRequiredData];
        expectedResult = service.addFinancialParameterToCollectionIfMissing(financialParameterCollection, ...financialParameterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const financialParameter: IFinancialParameter = sampleWithRequiredData;
        const financialParameter2: IFinancialParameter = sampleWithPartialData;
        expectedResult = service.addFinancialParameterToCollectionIfMissing([], financialParameter, financialParameter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(financialParameter);
        expect(expectedResult).toContain(financialParameter2);
      });

      it('should accept null and undefined values', () => {
        const financialParameter: IFinancialParameter = sampleWithRequiredData;
        expectedResult = service.addFinancialParameterToCollectionIfMissing([], null, financialParameter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(financialParameter);
      });

      it('should return initial array if no FinancialParameter is added', () => {
        const financialParameterCollection: IFinancialParameter[] = [sampleWithRequiredData];
        expectedResult = service.addFinancialParameterToCollectionIfMissing(financialParameterCollection, undefined, null);
        expect(expectedResult).toEqual(financialParameterCollection);
      });
    });

    describe('compareFinancialParameter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFinancialParameter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFinancialParameter(entity1, entity2);
        const compareResult2 = service.compareFinancialParameter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFinancialParameter(entity1, entity2);
        const compareResult2 = service.compareFinancialParameter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFinancialParameter(entity1, entity2);
        const compareResult2 = service.compareFinancialParameter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
