import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFinancialParameter } from '../financial-parameter.model';
import { FinancialParameterService } from '../service/financial-parameter.service';

@Component({
  standalone: true,
  templateUrl: './financial-parameter-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FinancialParameterDeleteDialogComponent {
  financialParameter?: IFinancialParameter;

  protected financialParameterService = inject(FinancialParameterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.financialParameterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
