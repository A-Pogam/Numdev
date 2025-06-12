import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormComponent } from './form.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from 'src/app/services/session.service';
import { Session } from '../../interfaces/session.interface';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';


describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let routerSpy: any;
  let snackBarSpy: any;

  beforeEach(async () => {
    routerSpy = { navigate: jest.fn(), url: '/sessions/create' };
    snackBarSpy = { open: jest.fn() };

    await TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatCardModule,       
        MatIconModule,  
        BrowserAnimationsModule
      ],
      declarations: [FormComponent],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
        {
          provide: SessionService,
          useValue: { sessionInformation: { admin: true } }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => null
              }
            }
          }
        },
        SessionApiService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize empty form on creation', () => {
    expect(component.sessionForm).toBeTruthy();
    expect(component.sessionForm?.value.name).toBe('');
  });

  it('should submit even if form is invalid (current behavior)', () => {
    component.sessionForm?.patchValue({
      name: '',
      date: '',
      teacher_id: '',
      description: ''
    });

    const spy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of({} as Session));
    component.submit();

    expect(component.sessionForm?.valid).toBeFalsy();
    expect(spy).toHaveBeenCalled(); // comportement actuel
  });

  it('should call create() on valid submission in creation mode', () => {
    const mockSession: Session = {
      name: 'Yoga du matin',
      date: new Date('2025-06-30'),
      teacher_id: 1,
      description: 'Séance de yoga matinale',
      users: [],
      id: 1
    };

    component.sessionForm?.setValue({
      name: mockSession.name,
      date: mockSession.date.toISOString().split('T')[0],
      teacher_id: mockSession.teacher_id,
      description: mockSession.description
    });

    const spy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));

    component.submit();

    expect(spy).toHaveBeenCalledWith({
      name: mockSession.name,
      date: mockSession.date.toISOString().split('T')[0],
      teacher_id: mockSession.teacher_id,
      description: mockSession.description
    });

    expect(snackBarSpy.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(routerSpy.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call update() on valid submission in edit mode', () => {
    // Simule l'édition
    (component as any)['onUpdate'] = true;
    (component as any)['id'] = '42';

    const mockSession: Session = {
      name: 'Yoga avancé',
      date: new Date('2025-07-15'),
      teacher_id: 2,
      description: 'Session avancée de yoga pour les experts',
      users: [],
      id: 42
    };

    component.sessionForm?.setValue({
      name: mockSession.name,
      date: mockSession.date.toISOString().split('T')[0],
      teacher_id: mockSession.teacher_id,
      description: mockSession.description
    });

    const spy = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(mockSession));

    component.submit();

    expect(spy).toHaveBeenCalledWith('42', {
      name: mockSession.name,
      date: mockSession.date.toISOString().split('T')[0],
      teacher_id: mockSession.teacher_id,
      description: mockSession.description
    });

    expect(snackBarSpy.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(routerSpy.navigate).toHaveBeenCalledWith(['sessions']);
  });
});
