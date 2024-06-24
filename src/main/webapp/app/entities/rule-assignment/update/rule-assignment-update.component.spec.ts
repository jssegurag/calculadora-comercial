import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IDroolsRuleFile } from 'app/entities/drools-rule-file/drools-rule-file.model';
import { DroolsRuleFileService } from 'app/entities/drools-rule-file/service/drools-rule-file.service';
import { RuleAssignmentService } from '../service/rule-assignment.service';
import { IRuleAssignment } from '../rule-assignment.model';
import { RuleAssignmentFormService } from './rule-assignment-form.service';

import { RuleAssignmentUpdateComponent } from './rule-assignment-update.component';

describe('RuleAssignment Management Update Component', () => {
  let comp: RuleAssignmentUpdateComponent;
  let fixture: ComponentFixture<RuleAssignmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ruleAssignmentFormService: RuleAssignmentFormService;
  let ruleAssignmentService: RuleAssignmentService;
  let droolsRuleFileService: DroolsRuleFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RuleAssignmentUpdateComponent],
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
      .overrideTemplate(RuleAssignmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RuleAssignmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ruleAssignmentFormService = TestBed.inject(RuleAssignmentFormService);
    ruleAssignmentService = TestBed.inject(RuleAssignmentService);
    droolsRuleFileService = TestBed.inject(DroolsRuleFileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DroolsRuleFile query and add missing value', () => {
      const ruleAssignment: IRuleAssignment = { id: 456 };
      const droolsRuleFile: IDroolsRuleFile = { id: 29360 };
      ruleAssignment.droolsRuleFile = droolsRuleFile;

      const droolsRuleFileCollection: IDroolsRuleFile[] = [{ id: 2099 }];
      jest.spyOn(droolsRuleFileService, 'query').mockReturnValue(of(new HttpResponse({ body: droolsRuleFileCollection })));
      const additionalDroolsRuleFiles = [droolsRuleFile];
      const expectedCollection: IDroolsRuleFile[] = [...additionalDroolsRuleFiles, ...droolsRuleFileCollection];
      jest.spyOn(droolsRuleFileService, 'addDroolsRuleFileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ruleAssignment });
      comp.ngOnInit();

      expect(droolsRuleFileService.query).toHaveBeenCalled();
      expect(droolsRuleFileService.addDroolsRuleFileToCollectionIfMissing).toHaveBeenCalledWith(
        droolsRuleFileCollection,
        ...additionalDroolsRuleFiles.map(expect.objectContaining),
      );
      expect(comp.droolsRuleFilesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ruleAssignment: IRuleAssignment = { id: 456 };
      const droolsRuleFile: IDroolsRuleFile = { id: 1151 };
      ruleAssignment.droolsRuleFile = droolsRuleFile;

      activatedRoute.data = of({ ruleAssignment });
      comp.ngOnInit();

      expect(comp.droolsRuleFilesSharedCollection).toContain(droolsRuleFile);
      expect(comp.ruleAssignment).toEqual(ruleAssignment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRuleAssignment>>();
      const ruleAssignment = { id: 123 };
      jest.spyOn(ruleAssignmentFormService, 'getRuleAssignment').mockReturnValue(ruleAssignment);
      jest.spyOn(ruleAssignmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruleAssignment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ruleAssignment }));
      saveSubject.complete();

      // THEN
      expect(ruleAssignmentFormService.getRuleAssignment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ruleAssignmentService.update).toHaveBeenCalledWith(expect.objectContaining(ruleAssignment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRuleAssignment>>();
      const ruleAssignment = { id: 123 };
      jest.spyOn(ruleAssignmentFormService, 'getRuleAssignment').mockReturnValue({ id: null });
      jest.spyOn(ruleAssignmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruleAssignment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ruleAssignment }));
      saveSubject.complete();

      // THEN
      expect(ruleAssignmentFormService.getRuleAssignment).toHaveBeenCalled();
      expect(ruleAssignmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRuleAssignment>>();
      const ruleAssignment = { id: 123 };
      jest.spyOn(ruleAssignmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruleAssignment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ruleAssignmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDroolsRuleFile', () => {
      it('Should forward to droolsRuleFileService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(droolsRuleFileService, 'compareDroolsRuleFile');
        comp.compareDroolsRuleFile(entity, entity2);
        expect(droolsRuleFileService.compareDroolsRuleFile).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
