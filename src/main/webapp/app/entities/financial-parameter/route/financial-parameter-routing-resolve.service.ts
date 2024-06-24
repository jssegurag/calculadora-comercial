import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinancialParameter } from '../financial-parameter.model';
import { FinancialParameterService } from '../service/financial-parameter.service';

const financialParameterResolve = (route: ActivatedRouteSnapshot): Observable<null | IFinancialParameter> => {
  const id = route.params['id'];
  if (id) {
    return inject(FinancialParameterService)
      .find(id)
      .pipe(
        mergeMap((financialParameter: HttpResponse<IFinancialParameter>) => {
          if (financialParameter.body) {
            return of(financialParameter.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default financialParameterResolve;
