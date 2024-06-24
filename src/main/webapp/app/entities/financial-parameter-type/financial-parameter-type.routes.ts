import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FinancialParameterTypeComponent } from './list/financial-parameter-type.component';
import { FinancialParameterTypeDetailComponent } from './detail/financial-parameter-type-detail.component';
import { FinancialParameterTypeUpdateComponent } from './update/financial-parameter-type-update.component';
import FinancialParameterTypeResolve from './route/financial-parameter-type-routing-resolve.service';

const financialParameterTypeRoute: Routes = [
  {
    path: '',
    component: FinancialParameterTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FinancialParameterTypeDetailComponent,
    resolve: {
      financialParameterType: FinancialParameterTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FinancialParameterTypeUpdateComponent,
    resolve: {
      financialParameterType: FinancialParameterTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FinancialParameterTypeUpdateComponent,
    resolve: {
      financialParameterType: FinancialParameterTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default financialParameterTypeRoute;
