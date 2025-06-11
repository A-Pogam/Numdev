import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with isLogged = false', (done) => {
    service.$isLogged().subscribe(value => {
      expect(value).toBe(false);
      done();
    });
  });

  it('should log in the user and set isLogged to true', (done) => {
    const mockUser: SessionInformation = {
      id: 1,
      username: 'testUser',
      email: 'test@example.com',
      // ajoute d'autres propriétés si l'interface en contient
    };

    service.logIn(mockUser);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockUser);

    service.$isLogged().subscribe(value => {
      expect(value).toBe(true);
      done();
    });
  });

  it('should log out the user and set isLogged to false', (done) => {
    const mockUser: SessionInformation = {
      id: 1,
      username: 'testUser',
      email: 'test@example.com',
    };

    service.logIn(mockUser); // se connecter d'abord
    service.logOut();        // puis se déconnecter

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();

    service.$isLogged().subscribe(value => {
      expect(value).toBe(false);
      done();
    });
  });
});
