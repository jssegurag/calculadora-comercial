import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BudgetComponent } from './list/budget.component';
import { BudgetDetailComponent } from './detail/budget-detail.component';
import { BudgetUpdateComponent } from './update/budget-update.component';
import BudgetResolve from './route/budget-routing-resolve.service';

const budgetRoute: Routes = [
  {
    path: '',
    component: BudgetComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BudgetDetailComponent,
    resolve: {
      budget: BudgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BudgetUpdateComponent,
    resolve: {
      budget: BudgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BudgetUpdateComponent,
    resolve: {
      budget: BudgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default budgetRoute;
