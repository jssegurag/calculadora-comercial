import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FinancialParameterTypeDetailComponent } from './financial-parameter-type-detail.component';

describe('FinancialParameterType Management Detail Component', () => {
  let comp: FinancialParameterTypeDetailComponent;
  let fixture: ComponentFixture<FinancialParameterTypeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinancialParameterTypeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FinancialParameterTypeDetailComponent,
              resolve: { financialParameterType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FinancialParameterTypeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FinancialParameterTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load financialParameterType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FinancialParameterTypeDetailComponent);

      // THEN
      expect(instance.financialParameterType()).toEqual(expect.objectContaining({ id: 123 }));
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
