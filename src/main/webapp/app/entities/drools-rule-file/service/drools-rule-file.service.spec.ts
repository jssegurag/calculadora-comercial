import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDroolsRuleFile } from '../drools-rule-file.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../drools-rule-file.test-samples';

import { DroolsRuleFileService, RestDroolsRuleFile } from './drools-rule-file.service';

const requireRestSample: RestDroolsRuleFile = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('DroolsRuleFile Service', () => {
  let service: DroolsRuleFileService;
  let httpMock: HttpTestingController;
  let expectedResult: IDroolsRuleFile | IDroolsRuleFile[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DroolsRuleFileService);
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

    it('should create a DroolsRuleFile', () => {
      const droolsRuleFile = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(droolsRuleFile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DroolsRuleFile', () => {
      const droolsRuleFile = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(droolsRuleFile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DroolsRuleFile', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DroolsRuleFile', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DroolsRuleFile', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDroolsRuleFileToCollectionIfMissing', () => {
      it('should add a DroolsRuleFile to an empty array', () => {
        const droolsRuleFile: IDroolsRuleFile = sampleWithRequiredData;
        expectedResult = service.addDroolsRuleFileToCollectionIfMissing([], droolsRuleFile);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(droolsRuleFile);
      });

      it('should not add a DroolsRuleFile to an array that contains it', () => {
        const droolsRuleFile: IDroolsRuleFile = sampleWithRequiredData;
        const droolsRuleFileCollection: IDroolsRuleFile[] = [
          {
            ...droolsRuleFile,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDroolsRuleFileToCollectionIfMissing(droolsRuleFileCollection, droolsRuleFile);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DroolsRuleFile to an array that doesn't contain it", () => {
        const droolsRuleFile: IDroolsRuleFile = sampleWithRequiredData;
        const droolsRuleFileCollection: IDroolsRuleFile[] = [sampleWithPartialData];
        expectedResult = service.addDroolsRuleFileToCollectionIfMissing(droolsRuleFileCollection, droolsRuleFile);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(droolsRuleFile);
      });

      it('should add only unique DroolsRuleFile to an array', () => {
        const droolsRuleFileArray: IDroolsRuleFile[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const droolsRuleFileCollection: IDroolsRuleFile[] = [sampleWithRequiredData];
        expectedResult = service.addDroolsRuleFileToCollectionIfMissing(droolsRuleFileCollection, ...droolsRuleFileArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const droolsRuleFile: IDroolsRuleFile = sampleWithRequiredData;
        const droolsRuleFile2: IDroolsRuleFile = sampleWithPartialData;
        expectedResult = service.addDroolsRuleFileToCollectionIfMissing([], droolsRuleFile, droolsRuleFile2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(droolsRuleFile);
        expect(expectedResult).toContain(droolsRuleFile2);
      });

      it('should accept null and undefined values', () => {
        const droolsRuleFile: IDroolsRuleFile = sampleWithRequiredData;
        expectedResult = service.addDroolsRuleFileToCollectionIfMissing([], null, droolsRuleFile, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(droolsRuleFile);
      });

      it('should return initial array if no DroolsRuleFile is added', () => {
        const droolsRuleFileCollection: IDroolsRuleFile[] = [sampleWithRequiredData];
        expectedResult = service.addDroolsRuleFileToCollectionIfMissing(droolsRuleFileCollection, undefined, null);
        expect(expectedResult).toEqual(droolsRuleFileCollection);
      });
    });

    describe('compareDroolsRuleFile', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDroolsRuleFile(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDroolsRuleFile(entity1, entity2);
        const compareResult2 = service.compareDroolsRuleFile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDroolsRuleFile(entity1, entity2);
        const compareResult2 = service.compareDroolsRuleFile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDroolsRuleFile(entity1, entity2);
        const compareResult2 = service.compareDroolsRuleFile(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
