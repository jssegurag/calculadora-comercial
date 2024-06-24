import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRuleAssignment } from '../rule-assignment.model';
import { RuleAssignmentService } from '../service/rule-assignment.service';

const ruleAssignmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IRuleAssignment> => {
  const id = route.params['id'];
  if (id) {
    return inject(RuleAssignmentService)
      .find(id)
      .pipe(
        mergeMap((ruleAssignment: HttpResponse<IRuleAssignment>) => {
          if (ruleAssignment.body) {
            return of(ruleAssignment.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ruleAssignmentResolve;
