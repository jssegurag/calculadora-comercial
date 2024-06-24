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
import { BudgetTemplateService } from '../service/budget-template.service';
import { IBudgetTemplate } from '../budget-template.model';
import { BudgetTemplateFormService, BudgetTemplateFormGroup } from './budget-template-form.service';

@Component({
  standalone: true,
  selector: 'jhi-budget-template-update',
  templateUrl: './budget-template-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BudgetTemplateUpdateComponent implements OnInit {
  isSaving = false;
  budgetTemplate: IBudgetTemplate | null = null;

  countriesSharedCollection: ICountry[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected budgetTemplateService = inject(BudgetTemplateService);
  protected budgetTemplateFormService = inject(BudgetTemplateFormService);
  protected countryService = inject(CountryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BudgetTemplateFormGroup = this.budgetTemplateFormService.createBudgetTemplateFormGroup();

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ budgetTemplate }) => {
      this.budgetTemplate = budgetTemplate;
      if (budgetTemplate) {
        this.updateForm(budgetTemplate);
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
    const budgetTemplate = this.budgetTemplateFormService.getBudgetTemplate(this.editForm);
    if (budgetTemplate.id !== null) {
      this.subscribeToSaveResponse(this.budgetTemplateService.update(budgetTemplate));
    } else {
      this.subscribeToSaveResponse(this.budgetTemplateService.create(budgetTemplate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBudgetTemplate>>): void {
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

  protected updateForm(budgetTemplate: IBudgetTemplate): void {
    this.budgetTemplate = budgetTemplate;
    this.budgetTemplateFormService.resetForm(this.editForm, budgetTemplate);

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      budgetTemplate.country,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.budgetTemplate?.country),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));
  }
}
