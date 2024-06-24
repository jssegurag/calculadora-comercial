import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserRoleComponent } from './list/user-role.component';
import { UserRoleDetailComponent } from './detail/user-role-detail.component';
import { UserRoleUpdateComponent } from './update/user-role-update.component';
import UserRoleResolve from './route/user-role-routing-resolve.service';

const userRoleRoute: Routes = [
  {
    path: '',
    component: UserRoleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserRoleDetailComponent,
    resolve: {
      userRole: UserRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserRoleUpdateComponent,
    resolve: {
      userRole: UserRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserRoleUpdateComponent,
    resolve: {
      userRole: UserRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userRoleRoute;
