import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';
import { IResource } from '../resource.model';
import { ResourceService } from '../service/resource.service';
import { ResourceFormService, ResourceFormGroup } from './resource-form.service';

@Component({
  standalone: true,
  selector: 'jhi-resource-update',
  templateUrl: './resource-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResourceUpdateComponent implements OnInit {
  isSaving = false;
  resource: IResource | null = null;

  positionsCollection: IPosition[] = [];

  protected resourceService = inject(ResourceService);
  protected resourceFormService = inject(ResourceFormService);
  protected positionService = inject(PositionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResourceFormGroup = this.resourceFormService.createResourceFormGroup();

  comparePosition = (o1: IPosition | null, o2: IPosition | null): boolean => this.positionService.comparePosition(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resource }) => {
      this.resource = resource;
      if (resource) {
        this.updateForm(resource);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resource = this.resourceFormService.getResource(this.editForm);
    if (resource.id !== null) {
      this.subscribeToSaveResponse(this.resourceService.update(resource));
    } else {
      this.subscribeToSaveResponse(this.resourceService.create(resource));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResource>>): void {
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

  protected updateForm(resource: IResource): void {
    this.resource = resource;
    this.resourceFormService.resetForm(this.editForm, resource);

    this.positionsCollection = this.positionService.addPositionToCollectionIfMissing<IPosition>(
      this.positionsCollection,
      resource.position,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.positionService
      .query({ 'resourceId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPosition[]>) => res.body ?? []))
      .pipe(
        map((positions: IPosition[]) =>
          this.positionService.addPositionToCollectionIfMissing<IPosition>(positions, this.resource?.position),
        ),
      )
      .subscribe((positions: IPosition[]) => (this.positionsCollection = positions));
  }
}
