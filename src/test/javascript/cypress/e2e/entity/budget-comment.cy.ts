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

describe('BudgetComment e2e test', () => {
  const budgetCommentPageUrl = '/budget-comment';
  const budgetCommentPageUrlPattern = new RegExp('/budget-comment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const budgetCommentSample = { content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let budgetComment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/budget-comments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/budget-comments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/budget-comments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (budgetComment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/budget-comments/${budgetComment.id}`,
      }).then(() => {
        budgetComment = undefined;
      });
    }
  });

  it('BudgetComments menu should load BudgetComments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('budget-comment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BudgetComment').should('exist');
    cy.url().should('match', budgetCommentPageUrlPattern);
  });

  describe('BudgetComment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(budgetCommentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BudgetComment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/budget-comment/new$'));
        cy.getEntityCreateUpdateHeading('BudgetComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetCommentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/budget-comments',
          body: budgetCommentSample,
        }).then(({ body }) => {
          budgetComment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/budget-comments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/budget-comments?page=0&size=20>; rel="last",<http://localhost/api/budget-comments?page=0&size=20>; rel="first"',
              },
              body: [budgetComment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(budgetCommentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BudgetComment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('budgetComment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetCommentPageUrlPattern);
      });

      it('edit button click should load edit BudgetComment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BudgetComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetCommentPageUrlPattern);
      });

      it.skip('edit button click should load edit BudgetComment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BudgetComment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetCommentPageUrlPattern);
      });

      it('last delete button click should delete instance of BudgetComment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('budgetComment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', budgetCommentPageUrlPattern);

        budgetComment = undefined;
      });
    });
  });

  describe('new BudgetComment page', () => {
    beforeEach(() => {
      cy.visit(`${budgetCommentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BudgetComment');
    });

    it('should create an instance of BudgetComment', () => {
      cy.get(`[data-cy="content"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="content"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="createdBy"]`).type('huzzah gosh');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'huzzah gosh');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-23T15:08');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-23T15:08');

      cy.get(`[data-cy="lastModifiedBy"]`).type('meh questionably quack');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'meh questionably quack');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T14:10');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T14:10');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        budgetComment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', budgetCommentPageUrlPattern);
    });
  });
});
