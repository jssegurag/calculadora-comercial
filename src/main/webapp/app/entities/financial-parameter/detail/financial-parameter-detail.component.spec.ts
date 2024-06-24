import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FinancialParameterDetailComponent } from './financial-parameter-detail.component';

describe('FinancialParameter Management Detail Component', () => {
  let comp: FinancialParameterDetailComponent;
  let fixture: ComponentFixture<FinancialParameterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinancialParameterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FinancialParameterDetailComponent,
              resolve: { financialParameter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FinancialParameterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FinancialParameterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load financialParameter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FinancialParameterDetailComponent);

      // THEN
      expect(instance.financialParameter()).toEqual(expect.objectContaining({ id: 123 }));
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
