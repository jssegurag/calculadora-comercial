import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { BudgetTemplateService } from '../service/budget-template.service';
import { IBudgetTemplate } from '../budget-template.model';
import { BudgetTemplateFormService } from './budget-template-form.service';

import { BudgetTemplateUpdateComponent } from './budget-template-update.component';

describe('BudgetTemplate Management Update Component', () => {
  let comp: BudgetTemplateUpdateComponent;
  let fixture: ComponentFixture<BudgetTemplateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let budgetTemplateFormService: BudgetTemplateFormService;
  let budgetTemplateService: BudgetTemplateService;
  let countryService: CountryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BudgetTemplateUpdateComponent],
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
      .overrideTemplate(BudgetTemplateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BudgetTemplateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    budgetTemplateFormService = TestBed.inject(BudgetTemplateFormService);
    budgetTemplateService = TestBed.inject(BudgetTemplateService);
    countryService = TestBed.inject(CountryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Country query and add missing value', () => {
      const budgetTemplate: IBudgetTemplate = { id: 456 };
      const country: ICountry = { id: 8928 };
      budgetTemplate.country = country;

      const countryCollection: ICountry[] = [{ id: 9917 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ budgetTemplate });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const budgetTemplate: IBudgetTemplate = { id: 456 };
      const country: ICountry = { id: 13937 };
      budgetTemplate.country = country;

      activatedRoute.data = of({ budgetTemplate });
      comp.ngOnInit();

      expect(comp.countriesSharedCollection).toContain(country);
      expect(comp.budgetTemplate).toEqual(budgetTemplate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetTemplate>>();
      const budgetTemplate = { id: 123 };
      jest.spyOn(budgetTemplateFormService, 'getBudgetTemplate').mockReturnValue(budgetTemplate);
      jest.spyOn(budgetTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budgetTemplate }));
      saveSubject.complete();

      // THEN
      expect(budgetTemplateFormService.getBudgetTemplate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(budgetTemplateService.update).toHaveBeenCalledWith(expect.objectContaining(budgetTemplate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetTemplate>>();
      const budgetTemplate = { id: 123 };
      jest.spyOn(budgetTemplateFormService, 'getBudgetTemplate').mockReturnValue({ id: null });
      jest.spyOn(budgetTemplateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetTemplate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budgetTemplate }));
      saveSubject.complete();

      // THEN
      expect(budgetTemplateFormService.getBudgetTemplate).toHaveBeenCalled();
      expect(budgetTemplateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudgetTemplate>>();
      const budgetTemplate = { id: 123 };
      jest.spyOn(budgetTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budgetTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(budgetTemplateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCountry', () => {
      it('Should forward to countryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(countryService, 'compareCountry');
        comp.compareCountry(entity, entity2);
        expect(countryService.compareCountry).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
