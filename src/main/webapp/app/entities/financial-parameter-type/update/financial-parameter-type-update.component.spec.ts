import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { FinancialParameterTypeService } from '../service/financial-parameter-type.service';
import { IFinancialParameterType } from '../financial-parameter-type.model';
import { FinancialParameterTypeFormService } from './financial-parameter-type-form.service';

import { FinancialParameterTypeUpdateComponent } from './financial-parameter-type-update.component';

describe('FinancialParameterType Management Update Component', () => {
  let comp: FinancialParameterTypeUpdateComponent;
  let fixture: ComponentFixture<FinancialParameterTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let financialParameterTypeFormService: FinancialParameterTypeFormService;
  let financialParameterTypeService: FinancialParameterTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, FinancialParameterTypeUpdateComponent],
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
      .overrideTemplate(FinancialParameterTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinancialParameterTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    financialParameterTypeFormService = TestBed.inject(FinancialParameterTypeFormService);
    financialParameterTypeService = TestBed.inject(FinancialParameterTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const financialParameterType: IFinancialParameterType = { id: 456 };

      activatedRoute.data = of({ financialParameterType });
      comp.ngOnInit();

      expect(comp.financialParameterType).toEqual(financialParameterType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialParameterType>>();
      const financialParameterType = { id: 123 };
      jest.spyOn(financialParameterTypeFormService, 'getFinancialParameterType').mockReturnValue(financialParameterType);
      jest.spyOn(financialParameterTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialParameterType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialParameterType }));
      saveSubject.complete();

      // THEN
      expect(financialParameterTypeFormService.getFinancialParameterType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(financialParameterTypeService.update).toHaveBeenCalledWith(expect.objectContaining(financialParameterType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialParameterType>>();
      const financialParameterType = { id: 123 };
      jest.spyOn(financialParameterTypeFormService, 'getFinancialParameterType').mockReturnValue({ id: null });
      jest.spyOn(financialParameterTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialParameterType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialParameterType }));
      saveSubject.complete();

      // THEN
      expect(financialParameterTypeFormService.getFinancialParameterType).toHaveBeenCalled();
      expect(financialParameterTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialParameterType>>();
      const financialParameterType = { id: 123 };
      jest.spyOn(financialParameterTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialParameterType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(financialParameterTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
