import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { IPermission } from '../permission.model';
import { PermissionService } from '../service/permission.service';
import { PermissionFormService, PermissionFormGroup } from './permission-form.service';

@Component({
  standalone: true,
  selector: 'jhi-permission-update',
  templateUrl: './permission-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PermissionUpdateComponent implements OnInit {
  isSaving = false;
  permission: IPermission | null = null;

  userRolesSharedCollection: IUserRole[] = [];

  protected permissionService = inject(PermissionService);
  protected permissionFormService = inject(PermissionFormService);
  protected userRoleService = inject(UserRoleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PermissionFormGroup = this.permissionFormService.createPermissionFormGroup();

  compareUserRole = (o1: IUserRole | null, o2: IUserRole | null): boolean => this.userRoleService.compareUserRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permission }) => {
      this.permission = permission;
      if (permission) {
        this.updateForm(permission);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permission = this.permissionFormService.getPermission(this.editForm);
    if (permission.id !== null) {
      this.subscribeToSaveResponse(this.permissionService.update(permission));
    } else {
      this.subscribeToSaveResponse(this.permissionService.create(permission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermission>>): void {
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

  protected updateForm(permission: IPermission): void {
    this.permission = permission;
    this.permissionFormService.resetForm(this.editForm, permission);

    this.userRolesSharedCollection = this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(
      this.userRolesSharedCollection,
      ...(permission.permissions ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userRoleService
      .query()
      .pipe(map((res: HttpResponse<IUserRole[]>) => res.body ?? []))
      .pipe(
        map((userRoles: IUserRole[]) =>
          this.userRoleService.addUserRoleToCollectionIfMissing<IUserRole>(userRoles, ...(this.permission?.permissions ?? [])),
        ),
      )
      .subscribe((userRoles: IUserRole[]) => (this.userRolesSharedCollection = userRoles));
  }
}
