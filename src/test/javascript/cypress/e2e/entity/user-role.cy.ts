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

describe('UserRole e2e test', () => {
  const userRolePageUrl = '/user-role';
  const userRolePageUrlPattern = new RegExp('/user-role(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userRoleSample = { name: 'interrogate scary victoriously' };

  let userRole;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-roles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-roles/${userRole.id}`,
      }).then(() => {
        userRole = undefined;
      });
    }
  });

  it('UserRoles menu should load UserRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserRole').should('exist');
    cy.url().should('match', userRolePageUrlPattern);
  });

  describe('UserRole page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-role/new$'));
        cy.getEntityCreateUpdateHeading('UserRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userRolePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-roles',
          body: userRoleSample,
        }).then(({ body }) => {
          userRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-roles?page=0&size=20>; rel="last",<http://localhost/api/user-roles?page=0&size=20>; rel="first"',
              },
              body: [userRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userRolePageUrlPattern);
      });

      it('edit button click should load edit UserRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userRolePageUrlPattern);
      });

      it.skip('edit button click should load edit UserRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userRolePageUrlPattern);
      });

      it('last delete button click should delete instance of UserRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('userRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userRolePageUrlPattern);

        userRole = undefined;
      });
    });
  });

  describe('new UserRole page', () => {
    beforeEach(() => {
      cy.visit(`${userRolePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserRole');
    });

    it('should create an instance of UserRole', () => {
      cy.get(`[data-cy="name"]`).type('absentmindedly');
      cy.get(`[data-cy="name"]`).should('have.value', 'absentmindedly');

      cy.get(`[data-cy="createdBy"]`).type('obnoxiously over traveler');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'obnoxiously over traveler');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-24T02:30');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-24T02:30');

      cy.get(`[data-cy="lastModifiedBy"]`).type('where');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'where');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T11:24');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T11:24');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userRolePageUrlPattern);
    });
  });
});
