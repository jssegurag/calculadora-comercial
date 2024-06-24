import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBudgetComment } from '../budget-comment.model';
import { BudgetCommentService } from '../service/budget-comment.service';

const budgetCommentResolve = (route: ActivatedRouteSnapshot): Observable<null | IBudgetComment> => {
  const id = route.params['id'];
  if (id) {
    return inject(BudgetCommentService)
      .find(id)
      .pipe(
        mergeMap((budgetComment: HttpResponse<IBudgetComment>) => {
          if (budgetComment.body) {
            return of(budgetComment.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default budgetCommentResolve;
