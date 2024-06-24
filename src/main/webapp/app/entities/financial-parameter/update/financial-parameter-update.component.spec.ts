import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFinancialParameterType } from 'app/entities/financial-parameter-type/financial-parameter-type.model';
import { FinancialParameterTypeService } from 'app/entities/financial-parameter-type/service/financial-parameter-type.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IUsers } from 'app/entities/users/users.model';
import { UsersService } from 'app/entities/users/service/users.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { IFinancialParameter } from '../financial-parameter.model';
import { FinancialParameterService } from '../service/financial-parameter.service';
import { FinancialParameterFormService } from './financial-parameter-form.service';

import { FinancialParameterUpdateComponent } from './financial-parameter-update.component';

describe('FinancialParameter Management Update Component', () => {
  let comp: FinancialParameterUpdateComponent;
  let fixture: ComponentFixture<FinancialParameterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let financialParameterFormService: FinancialParameterFormService;
  let financialParameterService: FinancialParameterService;
  let financialParameterTypeService: FinancialParameterTypeService;
  let countryService: CountryService;
  let usersService: UsersService;
  let userRoleService: UserRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, FinancialParameterUpdateComponent],
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
      .overrideTemplate(FinancialParameterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinancialParameterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    financialParameterFormService = TestBed.inject(FinancialParameterFormService);
    financialParameterService = TestBed.inject(FinancialParameterService);
    financialParameterTypeService = TestBed.inject(FinancialParameterTypeService);
    countryService = TestBed.inject(CountryService);
    usersService = TestBed.inject(UsersService);
    userRoleService = TestBed.inject(UserRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FinancialParameterType query and add missing value', () => {
      const financialParameter: IFinancialParameter = { id: 456 };
      const financialParameterType: IFinancialParameterType = { id: 31269 };
      financialParameter.financialParameterType = financialParameterType;

      const financialParameterTypeCollection: IFinancialParameterType[] = [{ id: 31501 }];
      jest.spyOn(financialParameterTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: financialParameterTypeCollection })));
      const additionalFinancialParameterTypes = [financialParameterType];
      const expectedCollection: IFinancialParameterType[] = [...additionalFinancialParameterTypes, ...financialParameterTypeCollection];
      jest.spyOn(financialParameterTypeService, 'addFinancialParameterTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ financialParameter });
      comp.ngOnInit();

      expect(financialParameterTypeService.query).toHaveBeenCalled();
      expect(financialParameterTypeService.addFinancialParameterTypeToCollectionIfMissing).toHaveBeenCalledWith(
        financialParameterTypeCollection,
        ...additionalFinancialParameterTypes.map(expect.objectContaining),
      );
      expect(comp.financialParameterTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Country query and add missing value', () => {
      const financialParameter: IFinancialParameter = { id: 456 };
      const country: ICountry = { id: 25179 };
      financialParameter.country = country;

      const countryCollection: ICountry[] = [{ id: 7792 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ financialParameter });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Users query and add missing value', () => {
      const financialParameter: IFinancialParameter = { id: 456 };
      const administrator: IUsers = { id: 2126 };
      financialParameter.administrator = administrator;

      const usersCollection: IUsers[] = [{ id: 30746 }];
      jest.spyOn(usersService, 'query').mockReturnValue(of(new HttpResponse({ body: usersCollection })));
      const additionalUsers = [administrator];
      const expectedCollection: IUsers[] = [...additionalUsers, ...usersCollection];
      jest.spyOn(usersService, 'addUsersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ financialParameter });
      comp.ngOnInit();

      expect(usersService.query).toHaveBeenCalled();
      expect(usersService.addUsersToCollectionIfMissing).toHaveBeenCalledWith(
        usersCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserRole query and add missing value', () => {
      const financialParameter: IFinancialParameter = { id: 456 };
      const roleAuthorizeds: IUserRole[] = [{ id: 23837 }];
      financialParameter.roleAuthorizeds = roleAuthorizeds;

      const userRoleCollection: IUserRole[] = [{ id: 32369 }];
      jest.spyOn(userRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: userRoleCollection })));
      const additionalUserRoles = [...roleAuthorizeds];
      const expectedCollection: IUserRole[] = [...additionalUserRoles, ...userRoleCollection];
      jest.spyOn(userRoleService, 'addUserRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ financialParameter });
      comp.ngOnInit();

      expect(userRoleService.query).toHaveBeenCalled();
      expect(userRoleService.addUserRoleToCollectionIfMissing).toHaveBeenCalledWith(
        userRoleCollection,
        ...additionalUserRoles.map(expect.objectContaining),
      );
      expect(comp.userRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const financialParameter: IFinancialParameter = { id: 456 };
      const financialParameterType: IFinancialParameterType = { id: 32541 };
      financialParameter.financialParameterType = financialParameterType;
      const country: ICountry = { id: 17624 };
      financialParameter.country = country;
      const administrator: IUsers = { id: 12216 };
      financialParameter.administrator = administrator;
      const roleAuthorized: IUserRole = { id: 32540 };
      financialParameter.roleAuthorizeds = [roleAuthorized];

      activatedRoute.data = of({ financialParameter });
      comp.ngOnInit();

      expect(comp.financialParameterTypesSharedCollection).toContain(financialParameterType);
      expect(comp.countriesSharedCollection).toContain(country);
      expect(comp.usersSharedCollection).toContain(administrator);
      expect(comp.userRolesSharedCollection).toContain(roleAuthorized);
      expect(comp.financialParameter).toEqual(financialParameter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialParameter>>();
      const financialParameter = { id: 123 };
      jest.spyOn(financialParameterFormService, 'getFinancialParameter').mockReturnValue(financialParameter);
      jest.spyOn(financialParameterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialParameter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialParameter }));
      saveSubject.complete();

      // THEN
      expect(financialParameterFormService.getFinancialParameter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(financialParameterService.update).toHaveBeenCalledWith(expect.objectContaining(financialParameter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialParameter>>();
      const financialParameter = { id: 123 };
      jest.spyOn(financialParameterFormService, 'getFinancialParameter').mockReturnValue({ id: null });
      jest.spyOn(financialParameterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialParameter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialParameter }));
      saveSubject.complete();

      // THEN
      expect(financialParameterFormService.getFinancialParameter).toHaveBeenCalled();
      expect(financialParameterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialParameter>>();
      const financialParameter = { id: 123 };
      jest.spyOn(financialParameterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialParameter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(financialParameterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFinancialParameterType', () => {
      it('Should forward to financialParameterTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(financialParameterTypeService, 'compareFinancialParameterType');
        comp.compareFinancialParameterType(entity, entity2);
        expect(financialParameterTypeService.compareFinancialParameterType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCountry', () => {
      it('Should forward to countryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(countryService, 'compareCountry');
        comp.compareCountry(entity, entity2);
        expect(countryService.compareCountry).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUsers', () => {
      it('Should forward to usersService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(usersService, 'compareUsers');
        comp.compareUsers(entity, entity2);
        expect(usersService.compareUsers).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUserRole', () => {
      it('Should forward to userRoleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userRoleService, 'compareUserRole');
        comp.compareUserRole(entity, entity2);
        expect(userRoleService.compareUserRole).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
