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

describe('Country e2e test', () => {
  const countryPageUrl = '/country';
  const countryPageUrlPattern = new RegExp('/country(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const countrySample = { name: 'silt insidious meh', active: true };

  let country;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/countries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/countries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/countries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (country) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/countries/${country.id}`,
      }).then(() => {
        country = undefined;
      });
    }
  });

  it('Countries menu should load Countries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('country');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Country').should('exist');
    cy.url().should('match', countryPageUrlPattern);
  });

  describe('Country page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(countryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Country page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/country/new$'));
        cy.getEntityCreateUpdateHeading('Country');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/countries',
          body: countrySample,
        }).then(({ body }) => {
          country = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/countries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/countries?page=0&size=20>; rel="last",<http://localhost/api/countries?page=0&size=20>; rel="first"',
              },
              body: [country],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(countryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Country page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('country');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);
      });

      it('edit button click should load edit Country page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Country');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);
      });

      it.skip('edit button click should load edit Country page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Country');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);
      });

      it('last delete button click should delete instance of Country', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('country').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', countryPageUrlPattern);

        country = undefined;
      });
    });
  });

  describe('new Country page', () => {
    beforeEach(() => {
      cy.visit(`${countryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Country');
    });

    it('should create an instance of Country', () => {
      cy.get(`[data-cy="name"]`).type('golden towards');
      cy.get(`[data-cy="name"]`).should('have.value', 'golden towards');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="createdBy"]`).type('substitute average');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'substitute average');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-23T21:21');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-23T21:21');

      cy.get(`[data-cy="lastModifiedBy"]`).type('outlandish uh-huh');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'outlandish uh-huh');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T14:32');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T14:32');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        country = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', countryPageUrlPattern);
    });
  });
});
