import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IFinancialParameterType } from '../financial-parameter-type.model';
import { FinancialParameterTypeService } from '../service/financial-parameter-type.service';

import financialParameterTypeResolve from './financial-parameter-type-routing-resolve.service';

describe('FinancialParameterType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: FinancialParameterTypeService;
  let resultFinancialParameterType: IFinancialParameterType | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(FinancialParameterTypeService);
    resultFinancialParameterType = undefined;
  });

  describe('resolve', () => {
    it('should return IFinancialParameterType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        financialParameterTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFinancialParameterType = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultFinancialParameterType).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        financialParameterTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFinancialParameterType = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFinancialParameterType).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IFinancialParameterType>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        financialParameterTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFinancialParameterType = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultFinancialParameterType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
