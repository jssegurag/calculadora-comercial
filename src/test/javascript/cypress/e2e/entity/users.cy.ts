import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Users e2e test', () => {
  const usersPageUrl = '/users';
  const usersPageUrlPattern = new RegExp('/users(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const usersSample = { name: 'impassioned', email: 'Grayson.Armstrong@gmail.com', password: 'labor absent till', active: true };

  let users;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (users) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${users.id}`,
      }).then(() => {
        users = undefined;
      });
    }
  });

  it('Users menu should load Users page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('users');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Users').should('exist');
    cy.url().should('match', usersPageUrlPattern);
  });

  describe('Users page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(usersPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Users page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/users/new$'));
        cy.getEntityCreateUpdateHeading('Users');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/users',
          body: usersSample,
        }).then(({ body }) => {
          users = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/users?page=0&size=20>; rel="last",<http://localhost/api/users?page=0&size=20>; rel="first"',
              },
              body: [users],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(usersPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Users page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('users');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);
      });

      it('edit button click should load edit Users page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Users');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);
      });

      it.skip('edit button click should load edit Users page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Users');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);
      });

      it('last delete button click should delete instance of Users', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('users').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);

        users = undefined;
      });
    });
  });

  describe('new Users page', () => {
    beforeEach(() => {
      cy.visit(`${usersPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Users');
    });

    it('should create an instance of Users', () => {
      cy.get(`[data-cy="name"]`).type('hay blaspheme');
      cy.get(`[data-cy="name"]`).should('have.value', 'hay blaspheme');

      cy.get(`[data-cy="email"]`).type('Gudrun.Upton-Senger@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Gudrun.Upton-Senger@hotmail.com');

      cy.get(`[data-cy="password"]`).type('tremendously');
      cy.get(`[data-cy="password"]`).should('have.value', 'tremendously');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="createdBy"]`).type('monthly pfft punctually');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'monthly pfft punctually');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-24T12:15');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-24T12:15');

      cy.get(`[data-cy="lastModifiedBy"]`).type('atop modernity nor');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'atop modernity nor');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T06:59');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T06:59');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        users = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', usersPageUrlPattern);
    });
  });
});
