import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IResourceAllocation } from '../resource-allocation.model';

@Component({
  standalone: true,
  selector: 'jhi-resource-allocation-detail',
  templateUrl: './resource-allocation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ResourceAllocationDetailComponent {
  resourceAllocation = input<IResourceAllocation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
