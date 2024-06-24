import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFinancialParameterType } from '../financial-parameter-type.model';

@Component({
  standalone: true,
  selector: 'jhi-financial-parameter-type-detail',
  templateUrl: './financial-parameter-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FinancialParameterTypeDetailComponent {
  financialParameterType = input<IFinancialParameterType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
