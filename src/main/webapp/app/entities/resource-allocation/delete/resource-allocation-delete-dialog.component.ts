import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IResourceAllocation } from '../resource-allocation.model';
import { ResourceAllocationService } from '../service/resource-allocation.service';

@Component({
  standalone: true,
  templateUrl: './resource-allocation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResourceAllocationDeleteDialogComponent {
  resourceAllocation?: IResourceAllocation;

  protected resourceAllocationService = inject(ResourceAllocationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resourceAllocationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
