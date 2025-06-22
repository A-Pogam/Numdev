describe('Page /me - Profil utilisateur', () => {
  beforeEach(() => {
    // Interception de la requête login 
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        email: 'user@example.com',
        firstName: 'Test',
        lastName: 'Test1',
        admin: false,
        token: 'fake-token'
      }
    }).as('login');
  });

  it('doit afficher les infos du user sur /me', () => {
    // Intercepte avant clic
    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        email: 'user@example.com',
        firstName: 'Test',
        lastName: 'Test1',
        admin: false
      }
    }).as('getUser');

    // Connexion utilisateur
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('user@example.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type=submit]').click();
    cy.wait('@login');

    // Clique sur "Account"
    cy.contains('Account').click();

    // Attente de récupération utilisateur
    cy.wait('@getUser');

    // Vérifie les infos affichées
    cy.contains('Test');
    cy.contains('TEST1');
    cy.contains('user@example.com');
  });
});
