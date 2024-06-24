import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { DroolsRuleFileService } from '../service/drools-rule-file.service';
import { IDroolsRuleFile } from '../drools-rule-file.model';
import { DroolsRuleFileFormService, DroolsRuleFileFormGroup } from './drools-rule-file-form.service';

@Component({
  standalone: true,
  selector: 'jhi-drools-rule-file-update',
  templateUrl: './drools-rule-file-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DroolsRuleFileUpdateComponent implements OnInit {
  isSaving = false;
  droolsRuleFile: IDroolsRuleFile | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected droolsRuleFileService = inject(DroolsRuleFileService);
  protected droolsRuleFileFormService = inject(DroolsRuleFileFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DroolsRuleFileFormGroup = this.droolsRuleFileFormService.createDroolsRuleFileFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ droolsRuleFile }) => {
      this.droolsRuleFile = droolsRuleFile;
      if (droolsRuleFile) {
        this.updateForm(droolsRuleFile);
      }
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
    const droolsRuleFile = this.droolsRuleFileFormService.getDroolsRuleFile(this.editForm);
    if (droolsRuleFile.id !== null) {
      this.subscribeToSaveResponse(this.droolsRuleFileService.update(droolsRuleFile));
    } else {
      this.subscribeToSaveResponse(this.droolsRuleFileService.create(droolsRuleFile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDroolsRuleFile>>): void {
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

  protected updateForm(droolsRuleFile: IDroolsRuleFile): void {
    this.droolsRuleFile = droolsRuleFile;
    this.droolsRuleFileFormService.resetForm(this.editForm, droolsRuleFile);
  }
}
