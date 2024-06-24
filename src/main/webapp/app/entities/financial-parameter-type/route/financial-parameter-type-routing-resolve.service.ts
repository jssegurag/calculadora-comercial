import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinancialParameterType } from '../financial-parameter-type.model';
import { FinancialParameterTypeService } from '../service/financial-parameter-type.service';

const financialParameterTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFinancialParameterType> => {
  const id = route.params['id'];
  if (id) {
    return inject(FinancialParameterTypeService)
      .find(id)
      .pipe(
        mergeMap((financialParameterType: HttpResponse<IFinancialParameterType>) => {
          if (financialParameterType.body) {
            return of(financialParameterType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default financialParameterTypeResolve;
