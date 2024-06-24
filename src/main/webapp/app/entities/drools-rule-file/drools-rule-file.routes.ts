import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DroolsRuleFileComponent } from './list/drools-rule-file.component';
import { DroolsRuleFileDetailComponent } from './detail/drools-rule-file-detail.component';
import { DroolsRuleFileUpdateComponent } from './update/drools-rule-file-update.component';
import DroolsRuleFileResolve from './route/drools-rule-file-routing-resolve.service';

const droolsRuleFileRoute: Routes = [
  {
    path: '',
    component: DroolsRuleFileComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DroolsRuleFileDetailComponent,
    resolve: {
      droolsRuleFile: DroolsRuleFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DroolsRuleFileUpdateComponent,
    resolve: {
      droolsRuleFile: DroolsRuleFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DroolsRuleFileUpdateComponent,
    resolve: {
      droolsRuleFile: DroolsRuleFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default droolsRuleFileRoute;
