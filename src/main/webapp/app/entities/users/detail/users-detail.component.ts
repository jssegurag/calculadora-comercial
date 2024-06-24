import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUsers } from '../users.model';

@Component({
  standalone: true,
  selector: 'jhi-users-detail',
  templateUrl: './users-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UsersDetailComponent {
  users = input<IUsers | null>(null);

  previousState(): void {
    window.history.back();
  }
}
