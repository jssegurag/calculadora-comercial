import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RuleAssignmentComponent } from './list/rule-assignment.component';
import { RuleAssignmentDetailComponent } from './detail/rule-assignment-detail.component';
import { RuleAssignmentUpdateComponent } from './update/rule-assignment-update.component';
import RuleAssignmentResolve from './route/rule-assignment-routing-resolve.service';

const ruleAssignmentRoute: Routes = [
  {
    path: '',
    component: RuleAssignmentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RuleAssignmentDetailComponent,
    resolve: {
      ruleAssignment: RuleAssignmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RuleAssignmentUpdateComponent,
    resolve: {
      ruleAssignment: RuleAssignmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RuleAssignmentUpdateComponent,
    resolve: {
      ruleAssignment: RuleAssignmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ruleAssignmentRoute;
