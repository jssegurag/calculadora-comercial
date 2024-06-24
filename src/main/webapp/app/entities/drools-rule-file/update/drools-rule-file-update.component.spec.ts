import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { DroolsRuleFileService } from '../service/drools-rule-file.service';
import { IDroolsRuleFile } from '../drools-rule-file.model';
import { DroolsRuleFileFormService } from './drools-rule-file-form.service';

import { DroolsRuleFileUpdateComponent } from './drools-rule-file-update.component';

describe('DroolsRuleFile Management Update Component', () => {
  let comp: DroolsRuleFileUpdateComponent;
  let fixture: ComponentFixture<DroolsRuleFileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let droolsRuleFileFormService: DroolsRuleFileFormService;
  let droolsRuleFileService: DroolsRuleFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, DroolsRuleFileUpdateComponent],
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
      .overrideTemplate(DroolsRuleFileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DroolsRuleFileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    droolsRuleFileFormService = TestBed.inject(DroolsRuleFileFormService);
    droolsRuleFileService = TestBed.inject(DroolsRuleFileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const droolsRuleFile: IDroolsRuleFile = { id: 456 };

      activatedRoute.data = of({ droolsRuleFile });
      comp.ngOnInit();

      expect(comp.droolsRuleFile).toEqual(droolsRuleFile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDroolsRuleFile>>();
      const droolsRuleFile = { id: 123 };
      jest.spyOn(droolsRuleFileFormService, 'getDroolsRuleFile').mockReturnValue(droolsRuleFile);
      jest.spyOn(droolsRuleFileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ droolsRuleFile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: droolsRuleFile }));
      saveSubject.complete();

      // THEN
      expect(droolsRuleFileFormService.getDroolsRuleFile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(droolsRuleFileService.update).toHaveBeenCalledWith(expect.objectContaining(droolsRuleFile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDroolsRuleFile>>();
      const droolsRuleFile = { id: 123 };
      jest.spyOn(droolsRuleFileFormService, 'getDroolsRuleFile').mockReturnValue({ id: null });
      jest.spyOn(droolsRuleFileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ droolsRuleFile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: droolsRuleFile }));
      saveSubject.complete();

      // THEN
      expect(droolsRuleFileFormService.getDroolsRuleFile).toHaveBeenCalled();
      expect(droolsRuleFileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDroolsRuleFile>>();
      const droolsRuleFile = { id: 123 };
      jest.spyOn(droolsRuleFileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ droolsRuleFile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(droolsRuleFileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
