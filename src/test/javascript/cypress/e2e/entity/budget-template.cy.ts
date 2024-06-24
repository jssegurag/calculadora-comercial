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

describe('BudgetTemplate e2e test', () => {
  const budgetTemplatePageUrl = '/budget-template';
  const budgetTemplatePageUrlPattern = new RegExp('/budget-template(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const budgetTemplateSample = { name: 'some realistic yum', active: true };

  let budgetTemplate;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/budget-templates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/budget-templates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/budget-templates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (budgetTemplate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/budget-templates/${budgetTemplate.id}`,
      }).then(() => {
        budgetTemplate = undefined;
      });
    }
  });

  it('BudgetTemplates menu should load BudgetTemplates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('budget-template');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BudgetTemplate').should('exist');
    cy.url().should('match', budgetTemplatePageUrlPattern);
  });

  describe('BudgetTemplate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(budgetTemplatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BudgetTemplate page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/budget-template/new$'));
        cy.getEntityCreateUpdateHeading('BudgetTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetTemplatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/budget-templates',
          body: budgetTemplateSample,
        }).then(({ body }) => {
          budgetTemplate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/budget-templates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/budget-templates?page=0&size=20>; rel="last",<http://localhost/api/budget-templates?page=0&size=20>; rel="first"',
              },
              body: [budgetTemplate],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(budgetTemplatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BudgetTemplate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('budgetTemplate');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetTemplatePageUrlPattern);
      });

      it('edit button click should load edit BudgetTemplate page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BudgetTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetTemplatePageUrlPattern);
      });

      it.skip('edit button click should load edit BudgetTemplate page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BudgetTemplate');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetTemplatePageUrlPattern);
      });

      it('last delete button click should delete instance of BudgetTemplate', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('budgetTemplate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetTemplatePageUrlPattern);

        budgetTemplate = undefined;
      });
    });
  });

  describe('new BudgetTemplate page', () => {
    beforeEach(() => {
      cy.visit(`${budgetTemplatePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BudgetTemplate');
    });

    it('should create an instance of BudgetTemplate', () => {
      cy.get(`[data-cy="name"]`).type('bah forge shrilly');
      cy.get(`[data-cy="name"]`).should('have.value', 'bah forge shrilly');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="startDate"]`).type('2024-06-24');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-06-24');

      cy.get(`[data-cy="endDate"]`).type('2024-06-23');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-06-23');

      cy.get(`[data-cy="estimatedDurationDays"]`).type('16391');
      cy.get(`[data-cy="estimatedDurationDays"]`).should('have.value', '16391');

      cy.get(`[data-cy="durationMonths"]`).type('12867');
      cy.get(`[data-cy="durationMonths"]`).should('have.value', '12867');

      cy.get(`[data-cy="monthlyHours"]`).type('12209.12');
      cy.get(`[data-cy="monthlyHours"]`).should('have.value', '12209.12');

      cy.get(`[data-cy="plannedHours"]`).type('10144.09');
      cy.get(`[data-cy="plannedHours"]`).should('have.value', '10144.09');

      cy.get(`[data-cy="resourceCount"]`).type('6759');
      cy.get(`[data-cy="resourceCount"]`).should('have.value', '6759');

      cy.get(`[data-cy="income"]`).type('32639.22');
      cy.get(`[data-cy="income"]`).should('have.value', '32639.22');

      cy.get(`[data-cy="otherTaxes"]`).type('20350.41');
      cy.get(`[data-cy="otherTaxes"]`).should('have.value', '20350.41');

      cy.get(`[data-cy="descriptionOtherTaxes"]`).type('way around');
      cy.get(`[data-cy="descriptionOtherTaxes"]`).should('have.value', 'way around');

      cy.get(`[data-cy="withholdingTaxes"]`).type('13113.29');
      cy.get(`[data-cy="withholdingTaxes"]`).should('have.value', '13113.29');

      cy.get(`[data-cy="modAndCifCosts"]`).type('21458.75');
      cy.get(`[data-cy="modAndCifCosts"]`).should('have.value', '21458.75');

      cy.get(`[data-cy="grossProfit"]`).type('7535');
      cy.get(`[data-cy="grossProfit"]`).should('have.value', '7535');

      cy.get(`[data-cy="grossProfitPercentage"]`).type('7763.94');
      cy.get(`[data-cy="grossProfitPercentage"]`).should('have.value', '7763.94');

      cy.get(`[data-cy="grossProfitRule"]`).type('11575.28');
      cy.get(`[data-cy="grossProfitRule"]`).should('have.value', '11575.28');

      cy.get(`[data-cy="absorbedFixedCosts"]`).type('16504.46');
      cy.get(`[data-cy="absorbedFixedCosts"]`).should('have.value', '16504.46');

      cy.get(`[data-cy="otherExpenses"]`).type('16119.75');
      cy.get(`[data-cy="otherExpenses"]`).should('have.value', '16119.75');

      cy.get(`[data-cy="profitBeforeTax"]`).type('25834.91');
      cy.get(`[data-cy="profitBeforeTax"]`).should('have.value', '25834.91');

      cy.get(`[data-cy="estimatedTaxes"]`).type('30440.45');
      cy.get(`[data-cy="estimatedTaxes"]`).should('have.value', '30440.45');

      cy.get(`[data-cy="estimatedNetProfit"]`).type('4742.84');
      cy.get(`[data-cy="estimatedNetProfit"]`).should('have.value', '4742.84');

      cy.get(`[data-cy="netMarginPercentage"]`).type('15594.24');
      cy.get(`[data-cy="netMarginPercentage"]`).should('have.value', '15594.24');

      cy.get(`[data-cy="netMarginRule"]`).type('5207.64');
      cy.get(`[data-cy="netMarginRule"]`).should('have.value', '5207.64');

      cy.get(`[data-cy="commissionToReceive"]`).type('14970.07');
      cy.get(`[data-cy="commissionToReceive"]`).should('have.value', '14970.07');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="createdBy"]`).type('ew hiccup valiantly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'ew hiccup valiantly');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-23T18:16');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-23T18:16');

      cy.get(`[data-cy="lastModifiedBy"]`).type('excellent aha before');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'excellent aha before');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T14:22');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T14:22');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        budgetTemplate = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', budgetTemplatePageUrlPattern);
    });
  });
});
