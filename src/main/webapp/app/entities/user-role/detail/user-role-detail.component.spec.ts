import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserRoleDetailComponent } from './user-role-detail.component';

describe('UserRole Management Detail Component', () => {
  let comp: UserRoleDetailComponent;
  let fixture: ComponentFixture<UserRoleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserRoleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UserRoleDetailComponent,
              resolve: { userRole: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserRoleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserRoleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userRole on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserRoleDetailComponent);

      // THEN
      expect(instance.userRole()).toEqual(expect.objectContaining({ id: 123 }));
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
