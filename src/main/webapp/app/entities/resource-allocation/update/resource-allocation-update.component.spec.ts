import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';
import { IBudgetTemplate } from 'app/entities/budget-template/budget-template.model';
import { BudgetTemplateService } from 'app/entities/budget-template/service/budget-template.service';
import { IResourceAllocation } from '../resource-allocation.model';
import { ResourceAllocationService } from '../service/resource-allocation.service';
import { ResourceAllocationFormService } from './resource-allocation-form.service';

import { ResourceAllocationUpdateComponent } from './resource-allocation-update.component';

describe('ResourceAllocation Management Update Component', () => {
  let comp: ResourceAllocationUpdateComponent;
  let fixture: ComponentFixture<ResourceAllocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resourceAllocationFormService: ResourceAllocationFormService;
  let resourceAllocationService: ResourceAllocationService;
  let budgetService: BudgetService;
  let resourceService: ResourceService;
  let budgetTemplateService: BudgetTemplateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ResourceAllocationUpdateComponent],
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
      .overrideTemplate(ResourceAllocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResourceAllocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resourceAllocationFormService = TestBed.inject(ResourceAllocationFormService);
    resourceAllocationService = TestBed.inject(ResourceAllocationService);
    budgetService = TestBed.inject(BudgetService);
    resourceService = TestBed.inject(ResourceService);
    budgetTemplateService = TestBed.inject(BudgetTemplateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Budget query and add missing value', () => {
      const resourceAllocation: IResourceAllocation = { id: 456 };
      const budget: IBudget = { id: 20145 };
      resourceAllocation.budget = budget;

      const budgetCollection: IBudget[] = [{ id: 28652 }];
      jest.spyOn(budgetService, 'query').mockReturnValue(of(new HttpResponse({ body: budgetCollection })));
      const additionalBudgets = [budget];
      const expectedCollection: IBudget[] = [...additionalBudgets, ...budgetCollection];
      jest.spyOn(budgetService, 'addBudgetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resourceAllocation });
      comp.ngOnInit();

      expect(budgetService.query).toHaveBeenCalled();
      expect(budgetService.addBudgetToCollectionIfMissing).toHaveBeenCalledWith(
        budgetCollection,
        ...additionalBudgets.map(expect.objectContaining),
      );
      expect(comp.budgetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Resource query and add missing value', () => {
      const resourceAllocation: IResourceAllocation = { id: 456 };
      const resource: IResource = { id: 31420 };
      resourceAllocation.resource = resource;

      const resourceCollection: IResource[] = [{ id: 22300 }];
      jest.spyOn(resourceService, 'query').mockReturnValue(of(new HttpResponse({ body: resourceCollection })));
      const additionalResources = [resource];
      const expectedCollection: IResource[] = [...additionalResources, ...resourceCollection];
      jest.spyOn(resourceService, 'addResourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resourceAllocation });
      comp.ngOnInit();

      expect(resourceService.query).toHaveBeenCalled();
      expect(resourceService.addResourceToCollectionIfMissing).toHaveBeenCalledWith(
        resourceCollection,
        ...additionalResources.map(expect.objectContaining),
      );
      expect(comp.resourcesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BudgetTemplate query and add missing value', () => {
      const resourceAllocation: IResourceAllocation = { id: 456 };
      const budgetTemplate: IBudgetTemplate = { id: 25898 };
      resourceAllocation.budgetTemplate = budgetTemplate;

      const budgetTemplateCollection: IBudgetTemplate[] = [{ id: 31415 }];
      jest.spyOn(budgetTemplateService, 'query').mockReturnValue(of(new HttpResponse({ body: budgetTemplateCollection })));
      const additionalBudgetTemplates = [budgetTemplate];
      const expectedCollection: IBudgetTemplate[] = [...additionalBudgetTemplates, ...budgetTemplateCollection];
      jest.spyOn(budgetTemplateService, 'addBudgetTemplateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resourceAllocation });
      comp.ngOnInit();

      expect(budgetTemplateService.query).toHaveBeenCalled();
      expect(budgetTemplateService.addBudgetTemplateToCollectionIfMissing).toHaveBeenCalledWith(
        budgetTemplateCollection,
        ...additionalBudgetTemplates.map(expect.objectContaining),
      );
      expect(comp.budgetTemplatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resourceAllocation: IResourceAllocation = { id: 456 };
      const budget: IBudget = { id: 7996 };
      resourceAllocation.budget = budget;
      const resource: IResource = { id: 726 };
      resourceAllocation.resource = resource;
      const budgetTemplate: IBudgetTemplate = { id: 30455 };
      resourceAllocation.budgetTemplate = budgetTemplate;

      activatedRoute.data = of({ resourceAllocation });
      comp.ngOnInit();

      expect(comp.budgetsSharedCollection).toContain(budget);
      expect(comp.resourcesSharedCollection).toContain(resource);
      expect(comp.budgetTemplatesSharedCollection).toContain(budgetTemplate);
      expect(comp.resourceAllocation).toEqual(resourceAllocation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceAllocation>>();
      const resourceAllocation = { id: 123 };
      jest.spyOn(resourceAllocationFormService, 'getResourceAllocation').mockReturnValue(resourceAllocation);
      jest.spyOn(resourceAllocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceAllocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceAllocation }));
      saveSubject.complete();

      // THEN
      expect(resourceAllocationFormService.getResourceAllocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resourceAllocationService.update).toHaveBeenCalledWith(expect.objectContaining(resourceAllocation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceAllocation>>();
      const resourceAllocation = { id: 123 };
      jest.spyOn(resourceAllocationFormService, 'getResourceAllocation').mockReturnValue({ id: null });
      jest.spyOn(resourceAllocationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceAllocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceAllocation }));
      saveSubject.complete();

      // THEN
      expect(resourceAllocationFormService.getResourceAllocation).toHaveBeenCalled();
      expect(resourceAllocationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceAllocation>>();
      const resourceAllocation = { id: 123 };
      jest.spyOn(resourceAllocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceAllocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resourceAllocationService.update).toHaveBeenCalled();
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

    describe('compareResource', () => {
      it('Should forward to resourceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(resourceService, 'compareResource');
        comp.compareResource(entity, entity2);
        expect(resourceService.compareResource).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBudgetTemplate', () => {
      it('Should forward to budgetTemplateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(budgetTemplateService, 'compareBudgetTemplate');
        comp.compareBudgetTemplate(entity, entity2);
        expect(budgetTemplateService.compareBudgetTemplate).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
