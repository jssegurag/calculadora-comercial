import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRuleAssignment } from '../rule-assignment.model';

@Component({
  standalone: true,
  selector: 'jhi-rule-assignment-detail',
  templateUrl: './rule-assignment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RuleAssignmentDetailComponent {
  ruleAssignment = input<IRuleAssignment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
