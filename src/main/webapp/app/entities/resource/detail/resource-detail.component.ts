import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IResource } from '../resource.model';

@Component({
  standalone: true,
  selector: 'jhi-resource-detail',
  templateUrl: './resource-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ResourceDetailComponent {
  resource = input<IResource | null>(null);

  previousState(): void {
    window.history.back();
  }
}
