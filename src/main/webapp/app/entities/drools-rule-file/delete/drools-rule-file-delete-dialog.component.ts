import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDroolsRuleFile } from '../drools-rule-file.model';
import { DroolsRuleFileService } from '../service/drools-rule-file.service';

@Component({
  standalone: true,
  templateUrl: './drools-rule-file-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DroolsRuleFileDeleteDialogComponent {
  droolsRuleFile?: IDroolsRuleFile;

  protected droolsRuleFileService = inject(DroolsRuleFileService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.droolsRuleFileService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
