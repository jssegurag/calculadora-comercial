import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUserRole } from '../user-role.model';

@Component({
  standalone: true,
  selector: 'jhi-user-role-detail',
  templateUrl: './user-role-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserRoleDetailComponent {
  userRole = input<IUserRole | null>(null);

  previousState(): void {
    window.history.back();
  }
}
