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
import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { BudgetCommentService } from '../service/budget-comment.service';
import { IBudgetComment } from '../budget-comment.model';
import { BudgetCommentFormService, BudgetCommentFormGroup } from './budget-comment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-budget-comment-update',
  templateUrl: './budget-comment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BudgetCommentUpdateComponent implements OnInit {
  isSaving = false;
  budgetComment: IBudgetComment | null = null;

  budgetsSharedCollection: IBudget[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected budgetCommentService = inject(BudgetCommentService);
  protected budgetCommentFormService = inject(BudgetCommentFormService);
  protected budgetService = inject(BudgetService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BudgetCommentFormGroup = this.budgetCommentFormService.createBudgetCommentFormGroup();

  compareBudget = (o1: IBudget | null, o2: IBudget | null): boolean => this.budgetService.compareBudget(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ budgetComment }) => {
      this.budgetComment = budgetComment;
      if (budgetComment) {
        this.updateForm(budgetComment);
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
    const budgetComment = this.budgetCommentFormService.getBudgetComment(this.editForm);
    if (budgetComment.id !== null) {
      this.subscribeToSaveResponse(this.budgetCommentService.update(budgetComment));
    } else {
      this.subscribeToSaveResponse(this.budgetCommentService.create(budgetComment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBudgetComment>>): void {
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

  protected updateForm(budgetComment: IBudgetComment): void {
    this.budgetComment = budgetComment;
    this.budgetCommentFormService.resetForm(this.editForm, budgetComment);

    this.budgetsSharedCollection = this.budgetService.addBudgetToCollectionIfMissing<IBudget>(
      this.budgetsSharedCollection,
      budgetComment.budget,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.budgetService
      .query()
      .pipe(map((res: HttpResponse<IBudget[]>) => res.body ?? []))
      .pipe(map((budgets: IBudget[]) => this.budgetService.addBudgetToCollectionIfMissing<IBudget>(budgets, this.budgetComment?.budget)))
      .subscribe((budgets: IBudget[]) => (this.budgetsSharedCollection = budgets));
  }
}
