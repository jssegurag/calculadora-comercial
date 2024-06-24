import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { BudgetCommentService } from '../service/budget-comment.service';
import { IBudgetComment } from '../budget-comment.model';
import { BudgetCommentFormService } from './budget-comment-form.service';

import { BudgetCommentUpdateComponent } from './budget-comment-update.component';

describe('BudgetComment Management Update Component', () => {
  let comp: BudgetCommentUpdateComponent;
  let fixture: ComponentFixture<BudgetCommentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let budgetCommentFormService: BudgetCommentFormService;
  let budgetCommentService: BudgetCommentService;
  let budgetService: BudgetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BudgetCommentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BudgetCommentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BudgetCommentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    budgetCommentFormService = TestBed.inject(BudgetCommentFormService);
    budgetCommentService = TestBed.inject(BudgetCommentService);
    budgetService = TestBed.inject(BudgetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Budget query and add missing value', () => {
      const budgetComment: IBudgetComment = { id: 456 };
      const budget: IBudget = { id: 26281 };
      budgetComment.budget = budget;

      const budgetCollection: IBudget[] = [{ id: 29724 }];
      jest.spyOn(budgetService, 'query').mockReturnValue(of(new HttpResponse({ body: budgetCollection })));
      const additionalBudgets = [budget];
      const expectedCollection: IBudget[] = [...additionalBudgets, ...budgetCollection];
      jest.spyOn(budgetService, 'addBudgetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ budgetComment });
      comp.ngOnInit();

      expect(budgetService.query).toHaveBeenCalled();
      expect(budgetService.addBudgetToCollectionIfMissing).toHaveBeenCalledWith(
        budgetCollection,
        ...additionalBudgets.map(expect.objectContaining),
      );
      expect(comp.budgetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const budgetComment: IBudgetComment = { id: 456 };
      const budget: IBudget = { id: 10480 };
      budgetComment.budget = budget;

      activatedRoute.data = of({ budgetComment });
      comp.ngOnInit();

      expect(comp.budgetsSharedCollection).toContain(budget);
      expect(comp.budgetComment).toEqual(budgetComment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetComment>>();
      const budgetComment = { id: 123 };
      jest.spyOn(budgetCommentFormService, 'getBudgetComment').mockReturnValue(budgetComment);
      jest.spyOn(budgetCommentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budgetComment }));
      saveSubject.complete();

      // THEN
      expect(budgetCommentFormService.getBudgetComment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(budgetCommentService.update).toHaveBeenCalledWith(expect.objectContaining(budgetComment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetComment>>();
      const budgetComment = { id: 123 };
      jest.spyOn(budgetCommentFormService, 'getBudgetComment').mockReturnValue({ id: null });
      jest.spyOn(budgetCommentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetComment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budgetComment }));
      saveSubject.complete();

      // THEN
      expect(budgetCommentFormService.getBudgetComment).toHaveBeenCalled();
      expect(budgetCommentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetComment>>();
      const budgetComment = { id: 123 };
      jest.spyOn(budgetCommentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(budgetCommentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBudget', () => {
      it('Should forward to budgetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(budgetService, 'compareBudget');
        comp.compareBudget(entity, entity2);
        expect(budgetService.compareBudget).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
