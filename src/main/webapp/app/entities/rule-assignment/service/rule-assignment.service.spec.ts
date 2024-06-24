import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRuleAssignment } from '../rule-assignment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rule-assignment.test-samples';

import { RuleAssignmentService, RestRuleAssignment } from './rule-assignment.service';

const requireRestSample: RestRuleAssignment = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('RuleAssignment Service', () => {
  let service: RuleAssignmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IRuleAssignment | IRuleAssignment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RuleAssignmentService);
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

    it('should create a RuleAssignment', () => {
      const ruleAssignment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ruleAssignment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RuleAssignment', () => {
      const ruleAssignment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ruleAssignment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RuleAssignment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RuleAssignment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RuleAssignment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRuleAssignmentToCollectionIfMissing', () => {
      it('should add a RuleAssignment to an empty array', () => {
        const ruleAssignment: IRuleAssignment = sampleWithRequiredData;
        expectedResult = service.addRuleAssignmentToCollectionIfMissing([], ruleAssignment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ruleAssignment);
      });

      it('should not add a RuleAssignment to an array that contains it', () => {
        const ruleAssignment: IRuleAssignment = sampleWithRequiredData;
        const ruleAssignmentCollection: IRuleAssignment[] = [
          {
            ...ruleAssignment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRuleAssignmentToCollectionIfMissing(ruleAssignmentCollection, ruleAssignment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RuleAssignment to an array that doesn't contain it", () => {
        const ruleAssignment: IRuleAssignment = sampleWithRequiredData;
        const ruleAssignmentCollection: IRuleAssignment[] = [sampleWithPartialData];
        expectedResult = service.addRuleAssignmentToCollectionIfMissing(ruleAssignmentCollection, ruleAssignment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ruleAssignment);
      });

      it('should add only unique RuleAssignment to an array', () => {
        const ruleAssignmentArray: IRuleAssignment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ruleAssignmentCollection: IRuleAssignment[] = [sampleWithRequiredData];
        expectedResult = service.addRuleAssignmentToCollectionIfMissing(ruleAssignmentCollection, ...ruleAssignmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ruleAssignment: IRuleAssignment = sampleWithRequiredData;
        const ruleAssignment2: IRuleAssignment = sampleWithPartialData;
        expectedResult = service.addRuleAssignmentToCollectionIfMissing([], ruleAssignment, ruleAssignment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ruleAssignment);
        expect(expectedResult).toContain(ruleAssignment2);
      });

      it('should accept null and undefined values', () => {
        const ruleAssignment: IRuleAssignment = sampleWithRequiredData;
        expectedResult = service.addRuleAssignmentToCollectionIfMissing([], null, ruleAssignment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ruleAssignment);
      });

      it('should return initial array if no RuleAssignment is added', () => {
        const ruleAssignmentCollection: IRuleAssignment[] = [sampleWithRequiredData];
        expectedResult = service.addRuleAssignmentToCollectionIfMissing(ruleAssignmentCollection, undefined, null);
        expect(expectedResult).toEqual(ruleAssignmentCollection);
      });
    });

    describe('compareRuleAssignment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRuleAssignment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRuleAssignment(entity1, entity2);
        const compareResult2 = service.compareRuleAssignment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRuleAssignment(entity1, entity2);
        const compareResult2 = service.compareRuleAssignment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRuleAssignment(entity1, entity2);
        const compareResult2 = service.compareRuleAssignment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
