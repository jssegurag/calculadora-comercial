import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BudgetTemplateComponent } from './list/budget-template.component';
import { BudgetTemplateDetailComponent } from './detail/budget-template-detail.component';
import { BudgetTemplateUpdateComponent } from './update/budget-template-update.component';
import BudgetTemplateResolve from './route/budget-template-routing-resolve.service';

const budgetTemplateRoute: Routes = [
  {
    path: '',
    component: BudgetTemplateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BudgetTemplateDetailComponent,
    resolve: {
      budgetTemplate: BudgetTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BudgetTemplateUpdateComponent,
    resolve: {
      budgetTemplate: BudgetTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BudgetTemplateUpdateComponent,
    resolve: {
      budgetTemplate: BudgetTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default budgetTemplateRoute;
