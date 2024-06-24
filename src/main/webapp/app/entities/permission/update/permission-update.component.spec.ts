import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { PermissionService } from '../service/permission.service';
import { IPermission } from '../permission.model';
import { PermissionFormService } from './permission-form.service';

import { PermissionUpdateComponent } from './permission-update.component';

describe('Permission Management Update Component', () => {
  let comp: PermissionUpdateComponent;
  let fixture: ComponentFixture<PermissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let permissionFormService: PermissionFormService;
  let permissionService: PermissionService;
  let userRoleService: UserRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PermissionUpdateComponent],
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
      .overrideTemplate(PermissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    permissionFormService = TestBed.inject(PermissionFormService);
    permissionService = TestBed.inject(PermissionService);
    userRoleService = TestBed.inject(UserRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserRole query and add missing value', () => {
      const permission: IPermission = { id: 456 };
      const permissions: IUserRole[] = [{ id: 19709 }];
      permission.permissions = permissions;

      const userRoleCollection: IUserRole[] = [{ id: 17181 }];
      jest.spyOn(userRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: userRoleCollection })));
      const additionalUserRoles = [...permissions];
      const expectedCollection: IUserRole[] = [...additionalUserRoles, ...userRoleCollection];
      jest.spyOn(userRoleService, 'addUserRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ permission });
      comp.ngOnInit();

      expect(userRoleService.query).toHaveBeenCalled();
      expect(userRoleService.addUserRoleToCollectionIfMissing).toHaveBeenCalledWith(
        userRoleCollection,
        ...additionalUserRoles.map(expect.objectContaining),
      );
      expect(comp.userRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const permission: IPermission = { id: 456 };
      const permissions: IUserRole = { id: 10164 };
      permission.permissions = [permissions];

      activatedRoute.data = of({ permission });
      comp.ngOnInit();

      expect(comp.userRolesSharedCollection).toContain(permissions);
      expect(comp.permission).toEqual(permission);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermission>>();
      const permission = { id: 123 };
      jest.spyOn(permissionFormService, 'getPermission').mockReturnValue(permission);
      jest.spyOn(permissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permission }));
      saveSubject.complete();

      // THEN
      expect(permissionFormService.getPermission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(permissionService.update).toHaveBeenCalledWith(expect.objectContaining(permission));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermission>>();
      const permission = { id: 123 };
      jest.spyOn(permissionFormService, 'getPermission').mockReturnValue({ id: null });
      jest.spyOn(permissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permission }));
      saveSubject.complete();

      // THEN
      expect(permissionFormService.getPermission).toHaveBeenCalled();
      expect(permissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermission>>();
      const permission = { id: 123 };
      jest.spyOn(permissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(permissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
