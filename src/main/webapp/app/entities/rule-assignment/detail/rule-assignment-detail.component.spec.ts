import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RuleAssignmentDetailComponent } from './rule-assignment-detail.component';

describe('RuleAssignment Management Detail Component', () => {
  let comp: RuleAssignmentDetailComponent;
  let fixture: ComponentFixture<RuleAssignmentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RuleAssignmentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RuleAssignmentDetailComponent,
              resolve: { ruleAssignment: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RuleAssignmentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RuleAssignmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ruleAssignment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RuleAssignmentDetailComponent);

      // THEN
      expect(instance.ruleAssignment()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
