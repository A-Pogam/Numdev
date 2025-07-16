import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {of, throwError} from "rxjs";


describe('LoginComponent', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: { login: jest.Mock };
  let routerMock: { navigate: jest.Mock };
  let sessionServiceMock: { logIn: jest.Mock };

  beforeEach(async () => {
    authServiceMock = { login: jest.fn() };

    sessionServiceMock = { logIn: jest.fn() };

    routerMock = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle the submit button based on form validity', () => {
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');

    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');
    fixture.detectChanges();
    expect(submitButton.disabled).toBeTruthy(); 

    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['password'].setValue('validPassword123');
    fixture.detectChanges();
    expect(submitButton.disabled).toBeFalsy(); 
  });


  it('should not show the error message by default', () => {
    const errorMessage = fixture.nativeElement.querySelector('.error');
    expect(errorMessage).toBeFalsy();
  });

  it('should display validation errors when fields are empty', () => {
    const emailControl = component.form.controls['email'];
    const passwordControl = component.form.controls['password'];

    emailControl.setValue('');
    passwordControl.setValue('');
    fixture.detectChanges();

    expect(emailControl.invalid).toBeTruthy();
    expect(passwordControl.invalid).toBeTruthy();
  });

  it('should mark email and password fields as invalid when left empty', () => {

    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');

    fixture.detectChanges();

    const emailField = fixture.nativeElement.querySelector('input[formControlName="email"]');
    const passwordField = fixture.nativeElement.querySelector('input[formControlName="password"]');
    expect(emailField.classList).toContain('ng-invalid');
    expect(passwordField.classList).toContain('ng-invalid');
  });

 
  it('should navigate to /sessions on successful login', () => {
    const fakeResponse = {token: 'fake-token '};

    authServiceMock.login = jest.fn().mockReturnValue(of(fakeResponse));

    component.form.setValue({
      email: 'test@example.com',
      password: 'password12345'
    });

    component.submit();

    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password12345'
    });

    expect(sessionServiceMock.logIn).toHaveBeenCalledWith(fakeResponse);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should display an error message if authentication fails', () => {
    authServiceMock.login = jest.fn().mockReturnValue(throwError(() => new Error()));
    component.form.setValue({
      email: 'invalid@example.com',
      password: 'invalidpassword'
    });

    component.submit();

    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'invalid@example.com',
      password: 'invalidpassword'
    });


    expect(component.onError).toBe(true);

    fixture.detectChanges();
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeTruthy();
    expect(errorElement.textContent).toContain('An error occurred');
  });

});