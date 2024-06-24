import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResourceAllocation } from '../resource-allocation.model';
import { ResourceAllocationService } from '../service/resource-allocation.service';

const resourceAllocationResolve = (route: ActivatedRouteSnapshot): Observable<null | IResourceAllocation> => {
  const id = route.params['id'];
  if (id) {
    return inject(ResourceAllocationService)
      .find(id)
      .pipe(
        mergeMap((resourceAllocation: HttpResponse<IResourceAllocation>) => {
          if (resourceAllocation.body) {
            return of(resourceAllocation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default resourceAllocationResolve;
