import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FinancialParameterComponent } from './list/financial-parameter.component';
import { FinancialParameterDetailComponent } from './detail/financial-parameter-detail.component';
import { FinancialParameterUpdateComponent } from './update/financial-parameter-update.component';
import FinancialParameterResolve from './route/financial-parameter-routing-resolve.service';

const financialParameterRoute: Routes = [
  {
    path: '',
    component: FinancialParameterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FinancialParameterDetailComponent,
    resolve: {
      financialParameter: FinancialParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FinancialParameterUpdateComponent,
    resolve: {
      financialParameter: FinancialParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FinancialParameterUpdateComponent,
    resolve: {
      financialParameter: FinancialParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default financialParameterRoute;
