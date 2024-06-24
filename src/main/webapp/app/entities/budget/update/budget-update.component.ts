import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IUsers } from 'app/entities/users/users.model';
import { UsersService } from 'app/entities/users/service/users.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { BudgetService } from '../service/budget.service';
import { IBudget } from '../budget.model';
import { BudgetFormService, BudgetFormGroup } from './budget-form.service';

@Component({
  standalone: true,
  selector: 'jhi-budget-update',
  templateUrl: './budget-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BudgetUpdateComponent implements OnInit {
  isSaving = false;
  budget: IBudget | null = null;

  countriesSharedCollection: ICountry[] = [];
  usersSharedCollection: IUsers[] = [];
  userRolesSharedCollection: IUserRole[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected budgetService = inject(BudgetService);
  protected budgetFormService = inject(BudgetFormService);
  protected countryService = inject(CountryService);
  protected usersService = inject(UsersService);
  protected userRoleService = inject(UserRoleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BudgetFormGroup = this.budgetFormService.createBudgetFormGroup();

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareUsers = (o1: IUsers | null, o2: IUsers | null): boolean => this.usersService.compareUsers(o1, o2);

  compareUserRole = (o1: IUserRole | null, o2: IUserRole | null): boolean => this.userRoleService.compareUserRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ budget }) => {
      this.budget = budget;
      if (budget) {
        this.updateForm(budget);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('quotizoApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const budget = this.budgetFormService.getBudget(this.editForm);
    if (budget.id !== null) {
      this.subscribeToSaveResponse(this.budgetService.update(budget));
    } else {
      this.subscribeToSaveResponse(this.budgetService.create(budget));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBudget>>): void {
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

  protected updateForm(budget: IBudget): void {
    this.budget = budget;
    this.budgetFormService.resetForm(this.editForm, budget);

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      budget.contry,
    );
    this.usersSharedCollection = this.usersService.addUsersToCollectionIfMissing<IUsers>(
      this.usersSharedCollection,
      budget.userAssignedTo,
      budget.userApprovedBy,
      budget.userOwner,
      ...(budget.authorizeds ?? []),
    );
    this.userRolesSharedCollection = this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(
      this.userRolesSharedCollection,
      ...(budget.roleAuthorizeds ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.budget?.contry)))
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.usersService
      .query()
      .pipe(map((res: HttpResponse<IUsers[]>) => res.body ?? []))
      .pipe(
        map((users: IUsers[]) =>
          this.usersService.addUsersToCollectionIfMissing<IUsers>(
            users,
            this.budget?.userAssignedTo,
            this.budget?.userApprovedBy,
            this.budget?.userOwner,
            ...(this.budget?.authorizeds ?? []),
          ),
        ),
      )
      .subscribe((users: IUsers[]) => (this.usersSharedCollection = users));

    this.userRoleService
      .query()
      .pipe(map((res: HttpResponse<IUserRole[]>) => res.body ?? []))
      .pipe(
        map((userRoles: IUserRole[]) =>
          this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(userRoles, ...(this.budget?.roleAuthorizeds ?? [])),
        ),
      )
      .subscribe((userRoles: IUserRole[]) => (this.userRolesSharedCollection = userRoles));
  }
}
