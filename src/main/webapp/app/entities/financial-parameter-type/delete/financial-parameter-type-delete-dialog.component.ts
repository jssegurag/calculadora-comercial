import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFinancialParameterType } from '../financial-parameter-type.model';
import { FinancialParameterTypeService } from '../service/financial-parameter-type.service';

@Component({
  standalone: true,
  templateUrl: './financial-parameter-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FinancialParameterTypeDeleteDialogComponent {
  financialParameterType?: IFinancialParameterType;

  protected financialParameterTypeService = inject(FinancialParameterTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.financialParameterTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
