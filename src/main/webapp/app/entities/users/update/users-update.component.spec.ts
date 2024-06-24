import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IBudget } from 'app/entities/budget/budget.model';
import { BudgetService } from 'app/entities/budget/service/budget.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { IUsers } from '../users.model';
import { UsersService } from '../service/users.service';
import { UsersFormService } from './users-form.service';

import { UsersUpdateComponent } from './users-update.component';

describe('Users Management Update Component', () => {
  let comp: UsersUpdateComponent;
  let fixture: ComponentFixture<UsersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usersFormService: UsersFormService;
  let usersService: UsersService;
  let budgetService: BudgetService;
  let userRoleService: UserRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, UsersUpdateComponent],
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
      .overrideTemplate(UsersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usersFormService = TestBed.inject(UsersFormService);
    usersService = TestBed.inject(UsersService);
    budgetService = TestBed.inject(BudgetService);
    userRoleService = TestBed.inject(UserRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Budget query and add missing value', () => {
      const users: IUsers = { id: 456 };
      const budgetAuthorizeds: IBudget[] = [{ id: 19977 }];
      users.budgetAuthorizeds = budgetAuthorizeds;

      const budgetCollection: IBudget[] = [{ id: 29945 }];
      jest.spyOn(budgetService, 'query').mockReturnValue(of(new HttpResponse({ body: budgetCollection })));
      const additionalBudgets = [...budgetAuthorizeds];
      const expectedCollection: IBudget[] = [...additionalBudgets, ...budgetCollection];
      jest.spyOn(budgetService, 'addBudgetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ users });
      comp.ngOnInit();

      expect(budgetService.query).toHaveBeenCalled();
      expect(budgetService.addBudgetToCollectionIfMissing).toHaveBeenCalledWith(
        budgetCollection,
        ...additionalBudgets.map(expect.objectContaining),
      );
      expect(comp.budgetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserRole query and add missing value', () => {
      const users: IUsers = { id: 456 };
      const userRoles: IUserRole[] = [{ id: 21862 }];
      users.userRoles = userRoles;

      const userRoleCollection: IUserRole[] = [{ id: 3549 }];
      jest.spyOn(userRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: userRoleCollection })));
      const additionalUserRoles = [...userRoles];
      const expectedCollection: IUserRole[] = [...additionalUserRoles, ...userRoleCollection];
      jest.spyOn(userRoleService, 'addUserRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ users });
      comp.ngOnInit();

      expect(userRoleService.query).toHaveBeenCalled();
      expect(userRoleService.addUserRoleToCollectionIfMissing).toHaveBeenCalledWith(
        userRoleCollection,
        ...additionalUserRoles.map(expect.objectContaining),
      );
      expect(comp.userRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const users: IUsers = { id: 456 };
      const budgetAuthorized: IBudget = { id: 10372 };
      users.budgetAuthorizeds = [budgetAuthorized];
      const userRole: IUserRole = { id: 31560 };
      users.userRoles = [userRole];

      activatedRoute.data = of({ users });
      comp.ngOnInit();

      expect(comp.budgetsSharedCollection).toContain(budgetAuthorized);
      expect(comp.userRolesSharedCollection).toContain(userRole);
      expect(comp.users).toEqual(users);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsers>>();
      const users = { id: 123 };
      jest.spyOn(usersFormService, 'getUsers').mockReturnValue(users);
      jest.spyOn(usersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ users });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: users }));
      saveSubject.complete();

      // THEN
      expect(usersFormService.getUsers).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(usersService.update).toHaveBeenCalledWith(expect.objectContaining(users));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsers>>();
      const users = { id: 123 };
      jest.spyOn(usersFormService, 'getUsers').mockReturnValue({ id: null });
      jest.spyOn(usersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ users: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: users }));
      saveSubject.complete();

      // THEN
      expect(usersFormService.getUsers).toHaveBeenCalled();
      expect(usersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsers>>();
      const users = { id: 123 };
      jest.spyOn(usersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ users });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usersService.update).toHaveBeenCalled();
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
