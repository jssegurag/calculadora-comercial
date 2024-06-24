import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';
import { IBudgetTemplate } from 'app/entities/budget-template/budget-template.model';
import { BudgetTemplateService } from 'app/entities/budget-template/service/budget-template.service';
import { ResourceAllocationService } from '../service/resource-allocation.service';
import { IResourceAllocation } from '../resource-allocation.model';
import { ResourceAllocationFormService, ResourceAllocationFormGroup } from './resource-allocation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-resource-allocation-update',
  templateUrl: './resource-allocation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResourceAllocationUpdateComponent implements OnInit {
  isSaving = false;
  resourceAllocation: IResourceAllocation | null = null;

  budgetsSharedCollection: IBudget[] = [];
  resourcesSharedCollection: IResource[] = [];
  budgetTemplatesSharedCollection: IBudgetTemplate[] = [];

  protected resourceAllocationService = inject(ResourceAllocationService);
  protected resourceAllocationFormService = inject(ResourceAllocationFormService);
  protected budgetService = inject(BudgetService);
  protected resourceService = inject(ResourceService);
  protected budgetTemplateService = inject(BudgetTemplateService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResourceAllocationFormGroup = this.resourceAllocationFormService.createResourceAllocationFormGroup();

  compareBudget = (o1: IBudget | null, o2: IBudget | null): boolean => this.budgetService.compareBudget(o1, o2);

  compareResource = (o1: IResource | null, o2: IResource | null): boolean => this.resourceService.compareResource(o1, o2);

  compareBudgetTemplate = (o1: IBudgetTemplate | null, o2: IBudgetTemplate | null): boolean =>
    this.budgetTemplateService.compareBudgetTemplate(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourceAllocation }) => {
      this.resourceAllocation = resourceAllocation;
      if (resourceAllocation) {
        this.updateForm(resourceAllocation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resourceAllocation = this.resourceAllocationFormService.getResourceAllocation(this.editForm);
    if (resourceAllocation.id !== null) {
      this.subscribeToSaveResponse(this.resourceAllocationService.update(resourceAllocation));
    } else {
      this.subscribeToSaveResponse(this.resourceAllocationService.create(resourceAllocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceAllocation>>): void {
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

  protected updateForm(resourceAllocation: IResourceAllocation): void {
    this.resourceAllocation = resourceAllocation;
    this.resourceAllocationFormService.resetForm(this.editForm, resourceAllocation);

    this.budgetsSharedCollection = this.budgetService.addBudgetToCollectionIfMissing<IBudget>(
      this.budgetsSharedCollection,
      resourceAllocation.budget,
    );
    this.resourcesSharedCollection = this.resourceService.addResourceToCollectionIfMissing<IResource>(
      this.resourcesSharedCollection,
      resourceAllocation.resource,
    );
    this.budgetTemplatesSharedCollection = this.budgetTemplateService.addBudgetTemplateToCollectionIfMissing<IBudgetTemplate>(
      this.budgetTemplatesSharedCollection,
      resourceAllocation.budgetTemplate,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.budgetService
      .query()
      .pipe(map((res: HttpResponse<IBudget[]>) => res.body ?? []))
      .pipe(
        map((budgets: IBudget[]) => this.budgetService.addBudgetToCollectionIfMissing<IBudget>(budgets, this.resourceAllocation?.budget)),
      )
      .subscribe((budgets: IBudget[]) => (this.budgetsSharedCollection = budgets));

    this.resourceService
      .query()
      .pipe(map((res: HttpResponse<IResource[]>) => res.body ?? []))
      .pipe(
        map((resources: IResource[]) =>
          this.resourceService.addResourceToCollectionIfMissing<IResource>(resources, this.resourceAllocation?.resource),
        ),
      )
      .subscribe((resources: IResource[]) => (this.resourcesSharedCollection = resources));

    this.budgetTemplateService
      .query()
      .pipe(map((res: HttpResponse<IBudgetTemplate[]>) => res.body ?? []))
      .pipe(
        map((budgetTemplates: IBudgetTemplate[]) =>
          this.budgetTemplateService.addBudgetTemplateToCollectionIfMissing<IBudgetTemplate>(
            budgetTemplates,
            this.resourceAllocation?.budgetTemplate,
          ),
        ),
      )
      .subscribe((budgetTemplates: IBudgetTemplate[]) => (this.budgetTemplatesSharedCollection = budgetTemplates));
  }
}
