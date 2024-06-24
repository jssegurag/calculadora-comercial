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

describe('Budget e2e test', () => {
  const budgetPageUrl = '/budget';
  const budgetPageUrlPattern = new RegExp('/budget(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const budgetSample = { name: 'somewhere' };

  let budget;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/budgets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/budgets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/budgets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (budget) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/budgets/${budget.id}`,
      }).then(() => {
        budget = undefined;
      });
    }
  });

  it('Budgets menu should load Budgets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('budget');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Budget').should('exist');
    cy.url().should('match', budgetPageUrlPattern);
  });

  describe('Budget page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(budgetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Budget page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/budget/new$'));
        cy.getEntityCreateUpdateHeading('Budget');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/budgets',
          body: budgetSample,
        }).then(({ body }) => {
          budget = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/budgets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/budgets?page=0&size=20>; rel="last",<http://localhost/api/budgets?page=0&size=20>; rel="first"',
              },
              body: [budget],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(budgetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Budget page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('budget');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);
      });

      it('edit button click should load edit Budget page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Budget');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);
      });

      it.skip('edit button click should load edit Budget page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Budget');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);
      });

      it('last delete button click should delete instance of Budget', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('budget').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetPageUrlPattern);

        budget = undefined;
      });
    });
  });

  describe('new Budget page', () => {
    beforeEach(() => {
      cy.visit(`${budgetPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Budget');
    });

    it('should create an instance of Budget', () => {
      cy.get(`[data-cy="name"]`).type('blah er');
      cy.get(`[data-cy="name"]`).should('have.value', 'blah er');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="startDate"]`).type('2024-06-24');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-06-24');

      cy.get(`[data-cy="endDate"]`).type('2024-06-23');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-06-23');

      cy.get(`[data-cy="estimatedDurationDays"]`).type('14931');
      cy.get(`[data-cy="estimatedDurationDays"]`).should('have.value', '14931');

      cy.get(`[data-cy="durationMonths"]`).type('18642');
      cy.get(`[data-cy="durationMonths"]`).should('have.value', '18642');

      cy.get(`[data-cy="monthlyHours"]`).type('6572.22');
      cy.get(`[data-cy="monthlyHours"]`).should('have.value', '6572.22');

      cy.get(`[data-cy="plannedHours"]`).type('4285.59');
      cy.get(`[data-cy="plannedHours"]`).should('have.value', '4285.59');

      cy.get(`[data-cy="resourceCount"]`).type('27461');
      cy.get(`[data-cy="resourceCount"]`).should('have.value', '27461');

      cy.get(`[data-cy="income"]`).type('22944.56');
      cy.get(`[data-cy="income"]`).should('have.value', '22944.56');

      cy.get(`[data-cy="otherTaxes"]`).type('17418.53');
      cy.get(`[data-cy="otherTaxes"]`).should('have.value', '17418.53');

      cy.get(`[data-cy="descriptionOtherTaxes"]`).type('pension vice');
      cy.get(`[data-cy="descriptionOtherTaxes"]`).should('have.value', 'pension vice');

      cy.get(`[data-cy="withholdingTaxes"]`).type('2878.74');
      cy.get(`[data-cy="withholdingTaxes"]`).should('have.value', '2878.74');

      cy.get(`[data-cy="modAndCifCosts"]`).type('15074.23');
      cy.get(`[data-cy="modAndCifCosts"]`).should('have.value', '15074.23');

      cy.get(`[data-cy="grossProfit"]`).type('30055.71');
      cy.get(`[data-cy="grossProfit"]`).should('have.value', '30055.71');

      cy.get(`[data-cy="grossProfitPercentage"]`).type('15416.2');
      cy.get(`[data-cy="grossProfitPercentage"]`).should('have.value', '15416.2');

      cy.get(`[data-cy="grossProfitRule"]`).type('19811.05');
      cy.get(`[data-cy="grossProfitRule"]`).should('have.value', '19811.05');

      cy.get(`[data-cy="absorbedFixedCosts"]`).type('12022.18');
      cy.get(`[data-cy="absorbedFixedCosts"]`).should('have.value', '12022.18');

      cy.get(`[data-cy="otherExpenses"]`).type('10870.73');
      cy.get(`[data-cy="otherExpenses"]`).should('have.value', '10870.73');

      cy.get(`[data-cy="profitBeforeTax"]`).type('18338.87');
      cy.get(`[data-cy="profitBeforeTax"]`).should('have.value', '18338.87');

      cy.get(`[data-cy="estimatedTaxes"]`).type('29143.63');
      cy.get(`[data-cy="estimatedTaxes"]`).should('have.value', '29143.63');

      cy.get(`[data-cy="estimatedNetProfit"]`).type('7100.9');
      cy.get(`[data-cy="estimatedNetProfit"]`).should('have.value', '7100.9');

      cy.get(`[data-cy="netMarginPercentage"]`).type('57.67');
      cy.get(`[data-cy="netMarginPercentage"]`).should('have.value', '57.67');

      cy.get(`[data-cy="netMarginRule"]`).type('3396.39');
      cy.get(`[data-cy="netMarginRule"]`).should('have.value', '3396.39');

      cy.get(`[data-cy="commissionToReceive"]`).type('24191.06');
      cy.get(`[data-cy="commissionToReceive"]`).should('have.value', '24191.06');

      cy.get(`[data-cy="needsApproval"]`).should('not.be.checked');
      cy.get(`[data-cy="needsApproval"]`).click();
      cy.get(`[data-cy="needsApproval"]`).should('be.checked');

      cy.get(`[data-cy="approvalDecision"]`).type('few');
      cy.get(`[data-cy="approvalDecision"]`).should('have.value', 'few');

      cy.get(`[data-cy="approvalDate"]`).type('2024-06-24T12:59');
      cy.get(`[data-cy="approvalDate"]`).blur();
      cy.get(`[data-cy="approvalDate"]`).should('have.value', '2024-06-24T12:59');

      cy.get(`[data-cy="approvalTime"]`).type('2024-06-24T01:17');
      cy.get(`[data-cy="approvalTime"]`).blur();
      cy.get(`[data-cy="approvalTime"]`).should('have.value', '2024-06-24T01:17');

      cy.get(`[data-cy="approvalComments"]`).type('brr ha');
      cy.get(`[data-cy="approvalComments"]`).should('have.value', 'brr ha');

      cy.get(`[data-cy="approvalStatus"]`).type('be meh');
      cy.get(`[data-cy="approvalStatus"]`).should('have.value', 'be meh');

      cy.get(`[data-cy="createdBy"]`).type('loosely schlepp');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'loosely schlepp');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-23T17:43');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-23T17:43');

      cy.get(`[data-cy="lastModifiedBy"]`).type('jubilantly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'jubilantly');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-23T21:36');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-23T21:36');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        budget = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', budgetPageUrlPattern);
    });
  });
});
