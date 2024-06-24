import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDroolsRuleFile } from '../drools-rule-file.model';
import { DroolsRuleFileService } from '../service/drools-rule-file.service';

const droolsRuleFileResolve = (route: ActivatedRouteSnapshot): Observable<null | IDroolsRuleFile> => {
  const id = route.params['id'];
  if (id) {
    return inject(DroolsRuleFileService)
      .find(id)
      .pipe(
        mergeMap((droolsRuleFile: HttpResponse<IDroolsRuleFile>) => {
          if (droolsRuleFile.body) {
            return of(droolsRuleFile.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default droolsRuleFileResolve;
