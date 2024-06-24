import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPermission } from 'app/entities/permission/permission.model';
import { PermissionService } from 'app/entities/permission/service/permission.service';
import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { IFinancialParameter } from 'app/entities/financial-parameter/financial-parameter.model';
import { FinancialParameterService } from 'app/entities/financial-parameter/service/financial-parameter.service';
import { IUsers } from 'app/entities/users/users.model';
import { UsersService } from 'app/entities/users/service/users.service';
import { UserRoleService } from '../service/user-role.service';
import { IUserRole } from '../user-role.model';
import { UserRoleFormService, UserRoleFormGroup } from './user-role-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-role-update',
  templateUrl: './user-role-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserRoleUpdateComponent implements OnInit {
  isSaving = false;
  userRole: IUserRole | null = null;

  permissionsSharedCollection: IPermission[] = [];
  budgetsSharedCollection: IBudget[] = [];
  financialParametersSharedCollection: IFinancialParameter[] = [];
  usersSharedCollection: IUsers[] = [];

  protected userRoleService = inject(UserRoleService);
  protected userRoleFormService = inject(UserRoleFormService);
  protected permissionService = inject(PermissionService);
  protected budgetService = inject(BudgetService);
  protected financialParameterService = inject(FinancialParameterService);
  protected usersService = inject(UsersService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserRoleFormGroup = this.userRoleFormService.createUserRoleFormGroup();

  comparePermission = (o1: IPermission | null, o2: IPermission | null): boolean => this.permissionService.comparePermission(o1, o2);

  compareBudget = (o1: IBudget | null, o2: IBudget | null): boolean => this.budgetService.compareBudget(o1, o2);

  compareFinancialParameter = (o1: IFinancialParameter | null, o2: IFinancialParameter | null): boolean =>
    this.financialParameterService.compareFinancialParameter(o1, o2);

  compareUsers = (o1: IUsers | null, o2: IUsers | null): boolean => this.usersService.compareUsers(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userRole }) => {
      this.userRole = userRole;
      if (userRole) {
        this.updateForm(userRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userRole = this.userRoleFormService.getUserRole(this.editForm);
    if (userRole.id !== null) {
      this.subscribeToSaveResponse(this.userRoleService.update(userRole));
    } else {
      this.subscribeToSaveResponse(this.userRoleService.create(userRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserRole>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userRole: IUserRole): void {
    this.userRole = userRole;
    this.userRoleFormService.resetForm(this.editForm, userRole);

    this.permissionsSharedCollection = this.permissionService.addPermissionToCollectionIfMissing<IPermission>(
      this.permissionsSharedCollection,
      ...(userRole.permissions ?? []),
    );
    this.budgetsSharedCollection = this.budgetService.addBudgetToCollectionIfMissing<IBudget>(
      this.budgetsSharedCollection,
      ...(userRole.budgets ?? []),
    );
    this.financialParametersSharedCollection =
      this.financialParameterService.addFinancialParameterToCollectionIfMissing<IFinancialParameter>(
        this.financialParametersSharedCollection,
        ...(userRole.financialParameters ?? []),
      );
    this.usersSharedCollection = this.usersService.addUsersToCollectionIfMissing<IUsers>(
      this.usersSharedCollection,
      ...(userRole.users ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.permissionService
      .query()
      .pipe(map((res: HttpResponse<IPermission[]>) => res.body ?? []))
      .pipe(
        map((permissions: IPermission[]) =>
          this.permissionService.addPermissionToCollectionIfMissing<IPermission>(permissions, ...(this.userRole?.permissions ?? [])),
        ),
      )
      .subscribe((permissions: IPermission[]) => (this.permissionsSharedCollection = permissions));

    this.budgetService
      .query()
      .pipe(map((res: HttpResponse<IBudget[]>) => res.body ?? []))
      .pipe(
        map((budgets: IBudget[]) => this.budgetService.addBudgetToCollectionIfMissing<IBudget>(budgets, ...(this.userRole?.budgets ?? []))),
      )
      .subscribe((budgets: IBudget[]) => (this.budgetsSharedCollection = budgets));

    this.financialParameterService
      .query()
      .pipe(map((res: HttpResponse<IFinancialParameter[]>) => res.body ?? []))
      .pipe(
        map((financialParameters: IFinancialParameter[]) =>
          this.financialParameterService.addFinancialParameterToCollectionIfMissing<IFinancialParameter>(
            financialParameters,
            ...(this.userRole?.financialParameters ?? []),
          ),
        ),
      )
      .subscribe((financialParameters: IFinancialParameter[]) => (this.financialParametersSharedCollection = financialParameters));

    this.usersService
      .query()
      .pipe(map((res: HttpResponse<IUsers[]>) => res.body ?? []))
      .pipe(map((users: IUsers[]) => this.usersService.addUsersToCollectionIfMissing<IUsers>(users, ...(this.userRole?.users ?? []))))
      .subscribe((users: IUsers[]) => (this.usersSharedCollection = users));
  }
}
