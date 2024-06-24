import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ResourceAllocationDetailComponent } from './resource-allocation-detail.component';

describe('ResourceAllocation Management Detail Component', () => {
  let comp: ResourceAllocationDetailComponent;
  let fixture: ComponentFixture<ResourceAllocationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResourceAllocationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ResourceAllocationDetailComponent,
              resolve: { resourceAllocation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ResourceAllocationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceAllocationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resourceAllocation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ResourceAllocationDetailComponent);

      // THEN
      expect(instance.resourceAllocation()).toEqual(expect.objectContaining({ id: 123 }));
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
