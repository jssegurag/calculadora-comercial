import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { UsersService } from '../service/users.service';
import { IUsers } from '../users.model';
import { UsersFormService, UsersFormGroup } from './users-form.service';

@Component({
  standalone: true,
  selector: 'jhi-users-update',
  templateUrl: './users-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UsersUpdateComponent implements OnInit {
  isSaving = false;
  users: IUsers | null = null;

  budgetsSharedCollection: IBudget[] = [];
  userRolesSharedCollection: IUserRole[] = [];

  protected usersService = inject(UsersService);
  protected usersFormService = inject(UsersFormService);
  protected budgetService = inject(BudgetService);
  protected userRoleService = inject(UserRoleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UsersFormGroup = this.usersFormService.createUsersFormGroup();

  compareBudget = (o1: IBudget | null, o2: IBudget | null): boolean => this.budgetService.compareBudget(o1, o2);

  compareUserRole = (o1: IUserRole | null, o2: IUserRole | null): boolean => this.userRoleService.compareUserRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ users }) => {
      this.users = users;
      if (users) {
        this.updateForm(users);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const users = this.usersFormService.getUsers(this.editForm);
    if (users.id !== null) {
      this.subscribeToSaveResponse(this.usersService.update(users));
    } else {
      this.subscribeToSaveResponse(this.usersService.create(users));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsers>>): void {
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

  protected updateForm(users: IUsers): void {
    this.users = users;
    this.usersFormService.resetForm(this.editForm, users);

    this.budgetsSharedCollection = this.budgetService.addBudgetToCollectionIfMissing<IBudget>(
      this.budgetsSharedCollection,
      ...(users.budgetAuthorizeds ?? []),
    );
    this.userRolesSharedCollection = this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(
      this.userRolesSharedCollection,
      ...(users.userRoles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.budgetService
      .query()
      .pipe(map((res: HttpResponse<IBudget[]>) => res.body ?? []))
      .pipe(
        map((budgets: IBudget[]) =>
          this.budgetService.addBudgetToCollectionIfMissing<IBudget>(budgets, ...(this.users?.budgetAuthorizeds ?? [])),
        ),
      )
      .subscribe((budgets: IBudget[]) => (this.budgetsSharedCollection = budgets));

    this.userRoleService
      .query()
      .pipe(map((res: HttpResponse<IUserRole[]>) => res.body ?? []))
      .pipe(
        map((userRoles: IUserRole[]) =>
          this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(userRoles, ...(this.users?.userRoles ?? [])),
        ),
      )
      .subscribe((userRoles: IUserRole[]) => (this.userRolesSharedCollection = userRoles));
  }
}
