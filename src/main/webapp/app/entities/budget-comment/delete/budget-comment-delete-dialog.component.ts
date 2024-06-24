import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBudgetComment } from '../budget-comment.model';
import { BudgetCommentService } from '../service/budget-comment.service';

@Component({
  standalone: true,
  templateUrl: './budget-comment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BudgetCommentDeleteDialogComponent {
  budgetComment?: IBudgetComment;

  protected budgetCommentService = inject(BudgetCommentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.budgetCommentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
