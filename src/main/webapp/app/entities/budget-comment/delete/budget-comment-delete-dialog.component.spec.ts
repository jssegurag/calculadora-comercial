jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { BudgetCommentService } from '../service/budget-comment.service';

import { BudgetCommentDeleteDialogComponent } from './budget-comment-delete-dialog.component';

describe('BudgetComment Management Delete Component', () => {
  let comp: BudgetCommentDeleteDialogComponent;
  let fixture: ComponentFixture<BudgetCommentDeleteDialogComponent>;
  let service: BudgetCommentService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BudgetCommentDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(BudgetCommentDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BudgetCommentDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BudgetCommentService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
