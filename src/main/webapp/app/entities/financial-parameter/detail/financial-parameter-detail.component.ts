import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFinancialParameter } from '../financial-parameter.model';

@Component({
  standalone: true,
  selector: 'jhi-financial-parameter-detail',
  templateUrl: './financial-parameter-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FinancialParameterDetailComponent {
  financialParameter = input<IFinancialParameter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
