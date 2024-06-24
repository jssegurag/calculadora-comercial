import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BudgetCommentComponent } from './list/budget-comment.component';
import { BudgetCommentDetailComponent } from './detail/budget-comment-detail.component';
import { BudgetCommentUpdateComponent } from './update/budget-comment-update.component';
import BudgetCommentResolve from './route/budget-comment-routing-resolve.service';

const budgetCommentRoute: Routes = [
  {
    path: '',
    component: BudgetCommentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BudgetCommentDetailComponent,
    resolve: {
      budgetComment: BudgetCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BudgetCommentUpdateComponent,
    resolve: {
      budgetComment: BudgetCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BudgetCommentUpdateComponent,
    resolve: {
      budgetComment: BudgetCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default budgetCommentRoute;
