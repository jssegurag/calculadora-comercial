import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDroolsRuleFile } from 'app/entities/drools-rule-file/drools-rule-file.model';
import { DroolsRuleFileService } from 'app/entities/drools-rule-file/service/drools-rule-file.service';
import { IRuleAssignment } from '../rule-assignment.model';
import { RuleAssignmentService } from '../service/rule-assignment.service';
import { RuleAssignmentFormService, RuleAssignmentFormGroup } from './rule-assignment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-rule-assignment-update',
  templateUrl: './rule-assignment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RuleAssignmentUpdateComponent implements OnInit {
  isSaving = false;
  ruleAssignment: IRuleAssignment | null = null;

  droolsRuleFilesSharedCollection: IDroolsRuleFile[] = [];

  protected ruleAssignmentService = inject(RuleAssignmentService);
  protected ruleAssignmentFormService = inject(RuleAssignmentFormService);
  protected droolsRuleFileService = inject(DroolsRuleFileService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RuleAssignmentFormGroup = this.ruleAssignmentFormService.createRuleAssignmentFormGroup();

  compareDroolsRuleFile = (o1: IDroolsRuleFile | null, o2: IDroolsRuleFile | null): boolean =>
    this.droolsRuleFileService.compareDroolsRuleFile(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ruleAssignment }) => {
      this.ruleAssignment = ruleAssignment;
      if (ruleAssignment) {
        this.updateForm(ruleAssignment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ruleAssignment = this.ruleAssignmentFormService.getRuleAssignment(this.editForm);
    if (ruleAssignment.id !== null) {
      this.subscribeToSaveResponse(this.ruleAssignmentService.update(ruleAssignment));
    } else {
      this.subscribeToSaveResponse(this.ruleAssignmentService.create(ruleAssignment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRuleAssignment>>): void {
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

  protected updateForm(ruleAssignment: IRuleAssignment): void {
    this.ruleAssignment = ruleAssignment;
    this.ruleAssignmentFormService.resetForm(this.editForm, ruleAssignment);

    this.droolsRuleFilesSharedCollection = this.droolsRuleFileService.addDroolsRuleFileToCollectionIfMissing<IDroolsRuleFile>(
      this.droolsRuleFilesSharedCollection,
      ruleAssignment.droolsRuleFile,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.droolsRuleFileService
      .query()
      .pipe(map((res: HttpResponse<IDroolsRuleFile[]>) => res.body ?? []))
      .pipe(
        map((droolsRuleFiles: IDroolsRuleFile[]) =>
          this.droolsRuleFileService.addDroolsRuleFileToCollectionIfMissing<IDroolsRuleFile>(
            droolsRuleFiles,
            this.ruleAssignment?.droolsRuleFile,
          ),
        ),
      )
      .subscribe((droolsRuleFiles: IDroolsRuleFile[]) => (this.droolsRuleFilesSharedCollection = droolsRuleFiles));
  }
}
