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

describe('FinancialParameterType e2e test', () => {
  const financialParameterTypePageUrl = '/financial-parameter-type';
  const financialParameterTypePageUrlPattern = new RegExp('/financial-parameter-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const financialParameterTypeSample = { name: 'before out', active: false };

  let financialParameterType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/financial-parameter-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/financial-parameter-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/financial-parameter-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (financialParameterType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/financial-parameter-types/${financialParameterType.id}`,
      }).then(() => {
        financialParameterType = undefined;
      });
    }
  });

  it('FinancialParameterTypes menu should load FinancialParameterTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('financial-parameter-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FinancialParameterType').should('exist');
    cy.url().should('match', financialParameterTypePageUrlPattern);
  });

  describe('FinancialParameterType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(financialParameterTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FinancialParameterType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/financial-parameter-type/new$'));
        cy.getEntityCreateUpdateHeading('FinancialParameterType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/financial-parameter-types',
          body: financialParameterTypeSample,
        }).then(({ body }) => {
          financialParameterType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/financial-parameter-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/financial-parameter-types?page=0&size=20>; rel="last",<http://localhost/api/financial-parameter-types?page=0&size=20>; rel="first"',
              },
              body: [financialParameterType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(financialParameterTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FinancialParameterType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('financialParameterType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterTypePageUrlPattern);
      });

      it('edit button click should load edit FinancialParameterType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FinancialParameterType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterTypePageUrlPattern);
      });

      it.skip('edit button click should load edit FinancialParameterType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FinancialParameterType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FinancialParameterType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('financialParameterType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterTypePageUrlPattern);

        financialParameterType = undefined;
      });
    });
  });

  describe('new FinancialParameterType page', () => {
    beforeEach(() => {
      cy.visit(`${financialParameterTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FinancialParameterType');
    });

    it('should create an instance of FinancialParameterType', () => {
      cy.get(`[data-cy="name"]`).type('weekly');
      cy.get(`[data-cy="name"]`).should('have.value', 'weekly');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="createdBy"]`).type('between');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'between');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-24T14:36');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-24T14:36');

      cy.get(`[data-cy="lastModifiedBy"]`).type('aside gosh');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'aside gosh');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T09:13');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T09:13');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        financialParameterType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', financialParameterTypePageUrlPattern);
    });
  });
});
