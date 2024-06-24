import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFinancialParameterType } from 'app/entities/financial-parameter-type/financial-parameter-type.model';
import { FinancialParameterTypeService } from 'app/entities/financial-parameter-type/service/financial-parameter-type.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IUsers } from 'app/entities/users/users.model';
import { UsersService } from 'app/entities/users/service/users.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { FinancialParameterService } from '../service/financial-parameter.service';
import { IFinancialParameter } from '../financial-parameter.model';
import { FinancialParameterFormService, FinancialParameterFormGroup } from './financial-parameter-form.service';

@Component({
  standalone: true,
  selector: 'jhi-financial-parameter-update',
  templateUrl: './financial-parameter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FinancialParameterUpdateComponent implements OnInit {
  isSaving = false;
  financialParameter: IFinancialParameter | null = null;

  financialParameterTypesSharedCollection: IFinancialParameterType[] = [];
  countriesSharedCollection: ICountry[] = [];
  usersSharedCollection: IUsers[] = [];
  userRolesSharedCollection: IUserRole[] = [];

  protected financialParameterService = inject(FinancialParameterService);
  protected financialParameterFormService = inject(FinancialParameterFormService);
  protected financialParameterTypeService = inject(FinancialParameterTypeService);
  protected countryService = inject(CountryService);
  protected usersService = inject(UsersService);
  protected userRoleService = inject(UserRoleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FinancialParameterFormGroup = this.financialParameterFormService.createFinancialParameterFormGroup();

  compareFinancialParameterType = (o1: IFinancialParameterType | null, o2: IFinancialParameterType | null): boolean =>
    this.financialParameterTypeService.compareFinancialParameterType(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareUsers = (o1: IUsers | null, o2: IUsers | null): boolean => this.usersService.compareUsers(o1, o2);

  compareUserRole = (o1: IUserRole | null, o2: IUserRole | null): boolean => this.userRoleService.compareUserRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financialParameter }) => {
      this.financialParameter = financialParameter;
      if (financialParameter) {
        this.updateForm(financialParameter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const financialParameter = this.financialParameterFormService.getFinancialParameter(this.editForm);
    if (financialParameter.id !== null) {
      this.subscribeToSaveResponse(this.financialParameterService.update(financialParameter));
    } else {
      this.subscribeToSaveResponse(this.financialParameterService.create(financialParameter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinancialParameter>>): void {
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

  protected updateForm(financialParameter: IFinancialParameter): void {
    this.financialParameter = financialParameter;
    this.financialParameterFormService.resetForm(this.editForm, financialParameter);

    this.financialParameterTypesSharedCollection =
      this.financialParameterTypeService.addFinancialParameterTypeToCollectionIfMissing<IFinancialParameterType>(
        this.financialParameterTypesSharedCollection,
        financialParameter.financialParameterType,
      );
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      financialParameter.country,
    );
    this.usersSharedCollection = this.usersService.addUsersToCollectionIfMissing<IUsers>(
      this.usersSharedCollection,
      financialParameter.administrator,
    );
    this.userRolesSharedCollection = this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(
      this.userRolesSharedCollection,
      ...(financialParameter.roleAuthorizeds ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.financialParameterTypeService
      .query()
      .pipe(map((res: HttpResponse<IFinancialParameterType[]>) => res.body ?? []))
      .pipe(
        map((financialParameterTypes: IFinancialParameterType[]) =>
          this.financialParameterTypeService.addFinancialParameterTypeToCollectionIfMissing<IFinancialParameterType>(
            financialParameterTypes,
            this.financialParameter?.financialParameterType,
          ),
        ),
      )
      .subscribe(
        (financialParameterTypes: IFinancialParameterType[]) => (this.financialParameterTypesSharedCollection = financialParameterTypes),
      );

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.financialParameter?.country),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.usersService
      .query()
      .pipe(map((res: HttpResponse<IUsers[]>) => res.body ?? []))
      .pipe(
        map((users: IUsers[]) => this.usersService.addUsersToCollectionIfMissing<IUsers>(users, this.financialParameter?.administrator)),
      )
      .subscribe((users: IUsers[]) => (this.usersSharedCollection = users));

    this.userRoleService
      .query()
      .pipe(map((res: HttpResponse<IUserRole[]>) => res.body ?? []))
      .pipe(
        map((userRoles: IUserRole[]) =>
          this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(userRoles, ...(this.financialParameter?.roleAuthorizeds ?? [])),
        ),
      )
      .subscribe((userRoles: IUserRole[]) => (this.userRolesSharedCollection = userRoles));
  }
}
