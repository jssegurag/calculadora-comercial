import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBudgetComment } from '../budget-comment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../budget-comment.test-samples';

import { BudgetCommentService, RestBudgetComment } from './budget-comment.service';

const requireRestSample: RestBudgetComment = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('BudgetComment Service', () => {
  let service: BudgetCommentService;
  let httpMock: HttpTestingController;
  let expectedResult: IBudgetComment | IBudgetComment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BudgetCommentService);
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

    it('should create a BudgetComment', () => {
      const budgetComment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(budgetComment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BudgetComment', () => {
      const budgetComment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(budgetComment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BudgetComment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BudgetComment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BudgetComment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBudgetCommentToCollectionIfMissing', () => {
      it('should add a BudgetComment to an empty array', () => {
        const budgetComment: IBudgetComment = sampleWithRequiredData;
        expectedResult = service.addBudgetCommentToCollectionIfMissing([], budgetComment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(budgetComment);
      });

      it('should not add a BudgetComment to an array that contains it', () => {
        const budgetComment: IBudgetComment = sampleWithRequiredData;
        const budgetCommentCollection: IBudgetComment[] = [
          {
            ...budgetComment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBudgetCommentToCollectionIfMissing(budgetCommentCollection, budgetComment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BudgetComment to an array that doesn't contain it", () => {
        const budgetComment: IBudgetComment = sampleWithRequiredData;
        const budgetCommentCollection: IBudgetComment[] = [sampleWithPartialData];
        expectedResult = service.addBudgetCommentToCollectionIfMissing(budgetCommentCollection, budgetComment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(budgetComment);
      });

      it('should add only unique BudgetComment to an array', () => {
        const budgetCommentArray: IBudgetComment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const budgetCommentCollection: IBudgetComment[] = [sampleWithRequiredData];
        expectedResult = service.addBudgetCommentToCollectionIfMissing(budgetCommentCollection, ...budgetCommentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const budgetComment: IBudgetComment = sampleWithRequiredData;
        const budgetComment2: IBudgetComment = sampleWithPartialData;
        expectedResult = service.addBudgetCommentToCollectionIfMissing([], budgetComment, budgetComment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(budgetComment);
        expect(expectedResult).toContain(budgetComment2);
      });

      it('should accept null and undefined values', () => {
        const budgetComment: IBudgetComment = sampleWithRequiredData;
        expectedResult = service.addBudgetCommentToCollectionIfMissing([], null, budgetComment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(budgetComment);
      });

      it('should return initial array if no BudgetComment is added', () => {
        const budgetCommentCollection: IBudgetComment[] = [sampleWithRequiredData];
        expectedResult = service.addBudgetCommentToCollectionIfMissing(budgetCommentCollection, undefined, null);
        expect(expectedResult).toEqual(budgetCommentCollection);
      });
    });

    describe('compareBudgetComment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBudgetComment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBudgetComment(entity1, entity2);
        const compareResult2 = service.compareBudgetComment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBudgetComment(entity1, entity2);
        const compareResult2 = service.compareBudgetComment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBudgetComment(entity1, entity2);
        const compareResult2 = service.compareBudgetComment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
