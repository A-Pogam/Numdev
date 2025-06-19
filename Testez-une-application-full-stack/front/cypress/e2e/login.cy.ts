describe('Login spec', () => {
  it('Login successfull', () => {
    // Intercepte la requête POST /api/auth/login et renvoie un faux token + utilisateur
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'fake-valid-token',
        user: {
          id: 1,
          email: 'yoga@studio.com',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true
        }
      }
    }).as('login');

    // Intercepte les sessions
    cy.intercept('GET', '/api/session', []).as('session');

    // Lance la page de login
    cy.visit('/login');

    // Remplit le formulaire
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234{enter}");
    cy.get('button[type=submit]').click();

    // Attend la redirection ou les données
    cy.wait('@login');
    cy.url().should('include', '/sessions');
  });
});
