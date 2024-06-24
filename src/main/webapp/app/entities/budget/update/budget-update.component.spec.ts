import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IUsers } from 'app/entities/users/users.model';
import { UsersService } from 'app/entities/users/service/users.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { IBudget } from '../budget.model';
import { BudgetService } from '../service/budget.service';
import { BudgetFormService } from './budget-form.service';

import { BudgetUpdateComponent } from './budget-update.component';

describe('Budget Management Update Component', () => {
  let comp: BudgetUpdateComponent;
  let fixture: ComponentFixture<BudgetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let budgetFormService: BudgetFormService;
  let budgetService: BudgetService;
  let countryService: CountryService;
  let usersService: UsersService;
  let userRoleService: UserRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BudgetUpdateComponent],
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
      .overrideTemplate(BudgetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BudgetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    budgetFormService = TestBed.inject(BudgetFormService);
    budgetService = TestBed.inject(BudgetService);
    countryService = TestBed.inject(CountryService);
    usersService = TestBed.inject(UsersService);
    userRoleService = TestBed.inject(UserRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Country query and add missing value', () => {
      const budget: IBudget = { id: 456 };
      const contry: ICountry = { id: 13882 };
      budget.contry = contry;

      const countryCollection: ICountry[] = [{ id: 8402 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [contry];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Users query and add missing value', () => {
      const budget: IBudget = { id: 456 };
      const userAssignedTo: IUsers = { id: 1990 };
      budget.userAssignedTo = userAssignedTo;
      const userApprovedBy: IUsers = { id: 18622 };
      budget.userApprovedBy = userApprovedBy;
      const userOwner: IUsers = { id: 21628 };
      budget.userOwner = userOwner;
      const authorizeds: IUsers[] = [{ id: 12230 }];
      budget.authorizeds = authorizeds;

      const usersCollection: IUsers[] = [{ id: 26312 }];
      jest.spyOn(usersService, 'query').mockReturnValue(of(new HttpResponse({ body: usersCollection })));
      const additionalUsers = [userAssignedTo, userApprovedBy, userOwner, ...authorizeds];
      const expectedCollection: IUsers[] = [...additionalUsers, ...usersCollection];
      jest.spyOn(usersService, 'addUsersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(usersService.query).toHaveBeenCalled();
      expect(usersService.addUsersToCollectionIfMissing).toHaveBeenCalledWith(
        usersCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserRole query and add missing value', () => {
      const budget: IBudget = { id: 456 };
      const roleAuthorizeds: IUserRole[] = [{ id: 9043 }];
      budget.roleAuthorizeds = roleAuthorizeds;

      const userRoleCollection: IUserRole[] = [{ id: 12652 }];
      jest.spyOn(userRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: userRoleCollection })));
      const additionalUserRoles = [...roleAuthorizeds];
      const expectedCollection: IUserRole[] = [...additionalUserRoles, ...userRoleCollection];
      jest.spyOn(userRoleService, 'addUserRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(userRoleService.query).toHaveBeenCalled();
      expect(userRoleService.addUserRoleToCollectionIfMissing).toHaveBeenCalledWith(
        userRoleCollection,
        ...additionalUserRoles.map(expect.objectContaining),
      );
      expect(comp.userRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const budget: IBudget = { id: 456 };
      const contry: ICountry = { id: 12478 };
      budget.contry = contry;
      const userAssignedTo: IUsers = { id: 28240 };
      budget.userAssignedTo = userAssignedTo;
      const userApprovedBy: IUsers = { id: 31316 };
      budget.userApprovedBy = userApprovedBy;
      const userOwner: IUsers = { id: 19986 };
      budget.userOwner = userOwner;
      const authorized: IUsers = { id: 940 };
      budget.authorizeds = [authorized];
      const roleAuthorized: IUserRole = { id: 23494 };
      budget.roleAuthorizeds = [roleAuthorized];

      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      expect(comp.countriesSharedCollection).toContain(contry);
      expect(comp.usersSharedCollection).toContain(userAssignedTo);
      expect(comp.usersSharedCollection).toContain(userApprovedBy);
      expect(comp.usersSharedCollection).toContain(userOwner);
      expect(comp.usersSharedCollection).toContain(authorized);
      expect(comp.userRolesSharedCollection).toContain(roleAuthorized);
      expect(comp.budget).toEqual(budget);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudget>>();
      const budget = { id: 123 };
      jest.spyOn(budgetFormService, 'getBudget').mockReturnValue(budget);
      jest.spyOn(budgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budget }));
      saveSubject.complete();

      // THEN
      expect(budgetFormService.getBudget).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(budgetService.update).toHaveBeenCalledWith(expect.objectContaining(budget));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudget>>();
      const budget = { id: 123 };
      jest.spyOn(budgetFormService, 'getBudget').mockReturnValue({ id: null });
      jest.spyOn(budgetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budget: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: budget }));
      saveSubject.complete();

      // THEN
      expect(budgetFormService.getBudget).toHaveBeenCalled();
      expect(budgetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBudget>>();
      const budget = { id: 123 };
      jest.spyOn(budgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ budget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(budgetService.update).toHaveBeenCalled();
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
