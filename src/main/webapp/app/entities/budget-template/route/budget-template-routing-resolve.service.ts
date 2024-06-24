import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBudgetTemplate } from '../budget-template.model';
import { BudgetTemplateService } from '../service/budget-template.service';

const budgetTemplateResolve = (route: ActivatedRouteSnapshot): Observable<null | IBudgetTemplate> => {
  const id = route.params['id'];
  if (id) {
    return inject(BudgetTemplateService)
      .find(id)
      .pipe(
        mergeMap((budgetTemplate: HttpResponse<IBudgetTemplate>) => {
          if (budgetTemplate.body) {
            return of(budgetTemplate.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default budgetTemplateResolve;
