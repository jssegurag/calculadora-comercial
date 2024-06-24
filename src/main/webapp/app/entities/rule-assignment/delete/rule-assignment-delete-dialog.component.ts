import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRuleAssignment } from '../rule-assignment.model';
import { RuleAssignmentService } from '../service/rule-assignment.service';

@Component({
  standalone: true,
  templateUrl: './rule-assignment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RuleAssignmentDeleteDialogComponent {
  ruleAssignment?: IRuleAssignment;

  protected ruleAssignmentService = inject(RuleAssignmentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ruleAssignmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
