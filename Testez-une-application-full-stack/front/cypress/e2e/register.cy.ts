describe('Register and Login flow', () => {
  it('should register a new user and then login', () => {
    // Intercept register
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: {
        token: 'fake-register-token',
        user: {
          id: 2,
          email: 'new@user.com',
          firstName: 'New',
          lastName: 'User',
          admin: false
        }
      }
    }).as('register');

    // Intercept login
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-login-token',
        user: {
          id: 2,
          email: 'new@user.com',
          firstName: 'New',
          lastName: 'User',
          admin: false
        }
      }
    }).as('login');

    // Intercept sessions
    cy.intercept('GET', '/api/session', []).as('getSessions');

    // inscription ---
    cy.visit('/register');
    cy.get('input[formControlName=email]').type('new@user.com');
    cy.get('input[formControlName=firstName]').type('New');
    cy.get('input[formControlName=lastName]').type('User');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type=submit]').click();

    cy.wait('@register');

    // Vérifie que redirection vers /login a lieu
    cy.url({ timeout: 5000 }).should('include', '/login');

    // connexion ---
    cy.get('input[formControlName=email]').type('new@user.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type=submit]').click();

    cy.wait('@login');

    cy.window().then((win) => {
      win.localStorage.setItem('token', 'fake-login-token');
    });

    cy.url({ timeout: 5000 }).should('include', '/sessions');
  });
});
