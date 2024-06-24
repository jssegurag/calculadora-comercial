import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ResourceAllocationComponent } from './list/resource-allocation.component';
import { ResourceAllocationDetailComponent } from './detail/resource-allocation-detail.component';
import { ResourceAllocationUpdateComponent } from './update/resource-allocation-update.component';
import ResourceAllocationResolve from './route/resource-allocation-routing-resolve.service';

const resourceAllocationRoute: Routes = [
  {
    path: '',
    component: ResourceAllocationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResourceAllocationDetailComponent,
    resolve: {
      resourceAllocation: ResourceAllocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResourceAllocationUpdateComponent,
    resolve: {
      resourceAllocation: ResourceAllocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResourceAllocationUpdateComponent,
    resolve: {
      resourceAllocation: ResourceAllocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default resourceAllocationRoute;
