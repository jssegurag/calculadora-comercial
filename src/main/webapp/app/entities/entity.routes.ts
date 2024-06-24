import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'users',
    data: { pageTitle: 'quotizoApp.users.home.title' },
    loadChildren: () => import('./users/users.routes'),
  },
  {
    path: 'user-role',
    data: { pageTitle: 'quotizoApp.userRole.home.title' },
    loadChildren: () => import('./user-role/user-role.routes'),
  },
  {
    path: 'permission',
    data: { pageTitle: 'quotizoApp.permission.home.title' },
    loadChildren: () => import('./permission/permission.routes'),
  },
  {
    path: 'position',
    data: { pageTitle: 'quotizoApp.position.home.title' },
    loadChildren: () => import('./position/position.routes'),
  },
  {
    path: 'resource',
    data: { pageTitle: 'quotizoApp.resource.home.title' },
    loadChildren: () => import('./resource/resource.routes'),
  },
  {
    path: 'budget',
    data: { pageTitle: 'quotizoApp.budget.home.title' },
    loadChildren: () => import('./budget/budget.routes'),
  },
  {
    path: 'budget-template',
    data: { pageTitle: 'quotizoApp.budgetTemplate.home.title' },
    loadChildren: () => import('./budget-template/budget-template.routes'),
  },
  {
    path: 'resource-allocation',
    data: { pageTitle: 'quotizoApp.resourceAllocation.home.title' },
    loadChildren: () => import('./resource-allocation/resource-allocation.routes'),
  },
  {
    path: 'financial-parameter',
    data: { pageTitle: 'quotizoApp.financialParameter.home.title' },
    loadChildren: () => import('./financial-parameter/financial-parameter.routes'),
  },
  {
    path: 'financial-parameter-type',
    data: { pageTitle: 'quotizoApp.financialParameterType.home.title' },
    loadChildren: () => import('./financial-parameter-type/financial-parameter-type.routes'),
  },
  {
    path: 'drools-rule-file',
    data: { pageTitle: 'quotizoApp.droolsRuleFile.home.title' },
    loadChildren: () => import('./drools-rule-file/drools-rule-file.routes'),
  },
  {
    path: 'country',
    data: { pageTitle: 'quotizoApp.country.home.title' },
    loadChildren: () => import('./country/country.routes'),
  },
  {
    path: 'budget-comment',
    data: { pageTitle: 'quotizoApp.budgetComment.home.title' },
    loadChildren: () => import('./budget-comment/budget-comment.routes'),
  },
  {
    path: 'rule-assignment',
    data: { pageTitle: 'quotizoApp.ruleAssignment.home.title' },
    loadChildren: () => import('./rule-assignment/rule-assignment.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
