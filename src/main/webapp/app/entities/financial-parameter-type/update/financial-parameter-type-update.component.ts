import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFinancialParameterType } from '../financial-parameter-type.model';
import { FinancialParameterTypeService } from '../service/financial-parameter-type.service';
import { FinancialParameterTypeFormService, FinancialParameterTypeFormGroup } from './financial-parameter-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-financial-parameter-type-update',
  templateUrl: './financial-parameter-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FinancialParameterTypeUpdateComponent implements OnInit {
  isSaving = false;
  financialParameterType: IFinancialParameterType | null = null;

  protected financialParameterTypeService = inject(FinancialParameterTypeService);
  protected financialParameterTypeFormService = inject(FinancialParameterTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FinancialParameterTypeFormGroup = this.financialParameterTypeFormService.createFinancialParameterTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financialParameterType }) => {
      this.financialParameterType = financialParameterType;
      if (financialParameterType) {
        this.updateForm(financialParameterType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const financialParameterType = this.financialParameterTypeFormService.getFinancialParameterType(this.editForm);
    if (financialParameterType.id !== null) {
      this.subscribeToSaveResponse(this.financialParameterTypeService.update(financialParameterType));
    } else {
      this.subscribeToSaveResponse(this.financialParameterTypeService.create(financialParameterType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinancialParameterType>>): void {
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

  protected updateForm(financialParameterType: IFinancialParameterType): void {
    this.financialParameterType = financialParameterType;
    this.financialParameterTypeFormService.resetForm(this.editForm, financialParameterType);
  }
}
