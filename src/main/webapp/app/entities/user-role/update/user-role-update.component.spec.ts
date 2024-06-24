import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPermission } from 'app/entities/permission/permission.model';
import { PermissionService } from 'app/entities/permission/service/permission.service';
import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { IFinancialParameter } from 'app/entities/financial-parameter/financial-parameter.model';
import { FinancialParameterService } from 'app/entities/financial-parameter/service/financial-parameter.service';
import { IUsers } from 'app/entities/users/users.model';
import { UsersService } from 'app/entities/users/service/users.service';
import { IUserRole } from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';
import { UserRoleFormService } from './user-role-form.service';

import { UserRoleUpdateComponent } from './user-role-update.component';

describe('UserRole Management Update Component', () => {
  let comp: UserRoleUpdateComponent;
  let fixture: ComponentFixture<UserRoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userRoleFormService: UserRoleFormService;
  let userRoleService: UserRoleService;
  let permissionService: PermissionService;
  let budgetService: BudgetService;
  let financialParameterService: FinancialParameterService;
  let usersService: UsersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, UserRoleUpdateComponent],
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
      .overrideTemplate(UserRoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserRoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userRoleFormService = TestBed.inject(UserRoleFormService);
    userRoleService = TestBed.inject(UserRoleService);
    permissionService = TestBed.inject(PermissionService);
    budgetService = TestBed.inject(BudgetService);
    financialParameterService = TestBed.inject(FinancialParameterService);
    usersService = TestBed.inject(UsersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Permission query and add missing value', () => {
      const userRole: IUserRole = { id: 456 };
      const permissions: IPermission[] = [{ id: 27176 }];
      userRole.permissions = permissions;

      const permissionCollection: IPermission[] = [{ id: 15201 }];
      jest.spyOn(permissionService, 'query').mockReturnValue(of(new HttpResponse({ body: permissionCollection })));
      const additionalPermissions = [...permissions];
      const expectedCollection: IPermission[] = [...additionalPermissions, ...permissionCollection];
      jest.spyOn(permissionService, 'addPermissionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      expect(permissionService.query).toHaveBeenCalled();
      expect(permissionService.addPermissionToCollectionIfMissing).toHaveBeenCalledWith(
        permissionCollection,
        ...additionalPermissions.map(expect.objectContaining),
      );
      expect(comp.permissionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Budget query and add missing value', () => {
      const userRole: IUserRole = { id: 456 };
      const budgets: IBudget[] = [{ id: 11565 }];
      userRole.budgets = budgets;

      const budgetCollection: IBudget[] = [{ id: 607 }];
      jest.spyOn(budgetService, 'query').mockReturnValue(of(new HttpResponse({ body: budgetCollection })));
      const additionalBudgets = [...budgets];
      const expectedCollection: IBudget[] = [...additionalBudgets, ...budgetCollection];
      jest.spyOn(budgetService, 'addBudgetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      expect(budgetService.query).toHaveBeenCalled();
      expect(budgetService.addBudgetToCollectionIfMissing).toHaveBeenCalledWith(
        budgetCollection,
        ...additionalBudgets.map(expect.objectContaining),
      );
      expect(comp.budgetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FinancialParameter query and add missing value', () => {
      const userRole: IUserRole = { id: 456 };
      const financialParameters: IFinancialParameter[] = [{ id: 16164 }];
      userRole.financialParameters = financialParameters;

      const financialParameterCollection: IFinancialParameter[] = [{ id: 1512 }];
      jest.spyOn(financialParameterService, 'query').mockReturnValue(of(new HttpResponse({ body: financialParameterCollection })));
      const additionalFinancialParameters = [...financialParameters];
      const expectedCollection: IFinancialParameter[] = [...additionalFinancialParameters, ...financialParameterCollection];
      jest.spyOn(financialParameterService, 'addFinancialParameterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      expect(financialParameterService.query).toHaveBeenCalled();
      expect(financialParameterService.addFinancialParameterToCollectionIfMissing).toHaveBeenCalledWith(
        financialParameterCollection,
        ...additionalFinancialParameters.map(expect.objectContaining),
      );
      expect(comp.financialParametersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Users query and add missing value', () => {
      const userRole: IUserRole = { id: 456 };
      const users: IUsers[] = [{ id: 20505 }];
      userRole.users = users;

      const usersCollection: IUsers[] = [{ id: 27806 }];
      jest.spyOn(usersService, 'query').mockReturnValue(of(new HttpResponse({ body: usersCollection })));
      const additionalUsers = [...users];
      const expectedCollection: IUsers[] = [...additionalUsers, ...usersCollection];
      jest.spyOn(usersService, 'addUsersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      expect(usersService.query).toHaveBeenCalled();
      expect(usersService.addUsersToCollectionIfMissing).toHaveBeenCalledWith(
        usersCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userRole: IUserRole = { id: 456 };
      const permission: IPermission = { id: 18153 };
      userRole.permissions = [permission];
      const budget: IBudget = { id: 30817 };
      userRole.budgets = [budget];
      const financialParameter: IFinancialParameter = { id: 16037 };
      userRole.financialParameters = [financialParameter];
      const users: IUsers = { id: 620 };
      userRole.users = [users];

      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      expect(comp.permissionsSharedCollection).toContain(permission);
      expect(comp.budgetsSharedCollection).toContain(budget);
      expect(comp.financialParametersSharedCollection).toContain(financialParameter);
      expect(comp.usersSharedCollection).toContain(users);
      expect(comp.userRole).toEqual(userRole);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRole>>();
      const userRole = { id: 123 };
      jest.spyOn(userRoleFormService, 'getUserRole').mockReturnValue(userRole);
      jest.spyOn(userRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userRole }));
      saveSubject.complete();

      // THEN
      expect(userRoleFormService.getUserRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userRoleService.update).toHaveBeenCalledWith(expect.objectContaining(userRole));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRole>>();
      const userRole = { id: 123 };
      jest.spyOn(userRoleFormService, 'getUserRole').mockReturnValue({ id: null });
      jest.spyOn(userRoleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userRole }));
      saveSubject.complete();

      // THEN
      expect(userRoleFormService.getUserRole).toHaveBeenCalled();
      expect(userRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRole>>();
      const userRole = { id: 123 };
      jest.spyOn(userRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePermission', () => {
      it('Should forward to permissionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(permissionService, 'comparePermission');
        comp.comparePermission(entity, entity2);
        expect(permissionService.comparePermission).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBudget', () => {
      it('Should forward to budgetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(budgetService, 'compareBudget');
        comp.compareBudget(entity, entity2);
        expect(budgetService.compareBudget).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFinancialParameter', () => {
      it('Should forward to financialParameterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(financialParameterService, 'compareFinancialParameter');
        comp.compareFinancialParameter(entity, entity2);
        expect(financialParameterService.compareFinancialParameter).toHaveBeenCalledWith(entity, entity2);
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
  });
});
