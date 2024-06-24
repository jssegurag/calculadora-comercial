import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBudgetTemplate } from '../budget-template.model';
import { BudgetTemplateService } from '../service/budget-template.service';

@Component({
  standalone: true,
  templateUrl: './budget-template-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BudgetTemplateDeleteDialogComponent {
  budgetTemplate?: IBudgetTemplate;

  protected budgetTemplateService = inject(BudgetTemplateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.budgetTemplateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
