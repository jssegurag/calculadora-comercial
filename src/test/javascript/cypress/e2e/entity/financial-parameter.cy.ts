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

describe('FinancialParameter e2e test', () => {
  const financialParameterPageUrl = '/financial-parameter';
  const financialParameterPageUrlPattern = new RegExp('/financial-parameter(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const financialParameterSample = { name: 'blank hence ferociously', value: 1259.24, active: true, mandatory: false };

  let financialParameter;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/financial-parameters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/financial-parameters').as('postEntityRequest');
    cy.intercept('DELETE', '/api/financial-parameters/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (financialParameter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/financial-parameters/${financialParameter.id}`,
      }).then(() => {
        financialParameter = undefined;
      });
    }
  });

  it('FinancialParameters menu should load FinancialParameters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('financial-parameter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FinancialParameter').should('exist');
    cy.url().should('match', financialParameterPageUrlPattern);
  });

  describe('FinancialParameter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(financialParameterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FinancialParameter page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/financial-parameter/new$'));
        cy.getEntityCreateUpdateHeading('FinancialParameter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/financial-parameters',
          body: financialParameterSample,
        }).then(({ body }) => {
          financialParameter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/financial-parameters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/financial-parameters?page=0&size=20>; rel="last",<http://localhost/api/financial-parameters?page=0&size=20>; rel="first"',
              },
              body: [financialParameter],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(financialParameterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FinancialParameter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('financialParameter');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterPageUrlPattern);
      });

      it('edit button click should load edit FinancialParameter page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FinancialParameter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterPageUrlPattern);
      });

      it.skip('edit button click should load edit FinancialParameter page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FinancialParameter');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterPageUrlPattern);
      });

      it('last delete button click should delete instance of FinancialParameter', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('financialParameter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', financialParameterPageUrlPattern);

        financialParameter = undefined;
      });
    });
  });

  describe('new FinancialParameter page', () => {
    beforeEach(() => {
      cy.visit(`${financialParameterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FinancialParameter');
    });

    it('should create an instance of FinancialParameter', () => {
      cy.get(`[data-cy="name"]`).type('yieldingly zowie chopsticks');
      cy.get(`[data-cy="name"]`).should('have.value', 'yieldingly zowie chopsticks');

      cy.get(`[data-cy="value"]`).type('2962.13');
      cy.get(`[data-cy="value"]`).should('have.value', '2962.13');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="mandatory"]`).should('not.be.checked');
      cy.get(`[data-cy="mandatory"]`).click();
      cy.get(`[data-cy="mandatory"]`).should('be.checked');

      cy.get(`[data-cy="createdBy"]`).type('boo whoever whoever');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'boo whoever whoever');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-24T02:34');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-24T02:34');

      cy.get(`[data-cy="lastModifiedBy"]`).type('kissingly ambitious');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'kissingly ambitious');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T10:35');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T10:35');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        financialParameter = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', financialParameterPageUrlPattern);
    });
  });
});
