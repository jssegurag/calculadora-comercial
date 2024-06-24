import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBudgetTemplate } from '../budget-template.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../budget-template.test-samples';

import { BudgetTemplateService, RestBudgetTemplate } from './budget-template.service';

const requireRestSample: RestBudgetTemplate = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('BudgetTemplate Service', () => {
  let service: BudgetTemplateService;
  let httpMock: HttpTestingController;
  let expectedResult: IBudgetTemplate | IBudgetTemplate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BudgetTemplateService);
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

    it('should create a BudgetTemplate', () => {
      const budgetTemplate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(budgetTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BudgetTemplate', () => {
      const budgetTemplate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(budgetTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BudgetTemplate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BudgetTemplate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BudgetTemplate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBudgetTemplateToCollectionIfMissing', () => {
      it('should add a BudgetTemplate to an empty array', () => {
        const budgetTemplate: IBudgetTemplate = sampleWithRequiredData;
        expectedResult = service.addBudgetTemplateToCollectionIfMissing([], budgetTemplate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(budgetTemplate);
      });

      it('should not add a BudgetTemplate to an array that contains it', () => {
        const budgetTemplate: IBudgetTemplate = sampleWithRequiredData;
        const budgetTemplateCollection: IBudgetTemplate[] = [
          {
            ...budgetTemplate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBudgetTemplateToCollectionIfMissing(budgetTemplateCollection, budgetTemplate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BudgetTemplate to an array that doesn't contain it", () => {
        const budgetTemplate: IBudgetTemplate = sampleWithRequiredData;
        const budgetTemplateCollection: IBudgetTemplate[] = [sampleWithPartialData];
        expectedResult = service.addBudgetTemplateToCollectionIfMissing(budgetTemplateCollection, budgetTemplate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(budgetTemplate);
      });

      it('should add only unique BudgetTemplate to an array', () => {
        const budgetTemplateArray: IBudgetTemplate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const budgetTemplateCollection: IBudgetTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addBudgetTemplateToCollectionIfMissing(budgetTemplateCollection, ...budgetTemplateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const budgetTemplate: IBudgetTemplate = sampleWithRequiredData;
        const budgetTemplate2: IBudgetTemplate = sampleWithPartialData;
        expectedResult = service.addBudgetTemplateToCollectionIfMissing([], budgetTemplate, budgetTemplate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(budgetTemplate);
        expect(expectedResult).toContain(budgetTemplate2);
      });

      it('should accept null and undefined values', () => {
        const budgetTemplate: IBudgetTemplate = sampleWithRequiredData;
        expectedResult = service.addBudgetTemplateToCollectionIfMissing([], null, budgetTemplate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(budgetTemplate);
      });

      it('should return initial array if no BudgetTemplate is added', () => {
        const budgetTemplateCollection: IBudgetTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addBudgetTemplateToCollectionIfMissing(budgetTemplateCollection, undefined, null);
        expect(expectedResult).toEqual(budgetTemplateCollection);
      });
    });

    describe('compareBudgetTemplate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBudgetTemplate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBudgetTemplate(entity1, entity2);
        const compareResult2 = service.compareBudgetTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBudgetTemplate(entity1, entity2);
        const compareResult2 = service.compareBudgetTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBudgetTemplate(entity1, entity2);
        const compareResult2 = service.compareBudgetTemplate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
