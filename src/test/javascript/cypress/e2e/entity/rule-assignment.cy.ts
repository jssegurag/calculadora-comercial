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

describe('RuleAssignment e2e test', () => {
  const ruleAssignmentPageUrl = '/rule-assignment';
  const ruleAssignmentPageUrlPattern = new RegExp('/rule-assignment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ruleAssignmentSample = { entityName: 'ha versus until', entityId: 1304 };

  let ruleAssignment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rule-assignments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rule-assignments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rule-assignments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ruleAssignment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rule-assignments/${ruleAssignment.id}`,
      }).then(() => {
        ruleAssignment = undefined;
      });
    }
  });

  it('RuleAssignments menu should load RuleAssignments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rule-assignment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RuleAssignment').should('exist');
    cy.url().should('match', ruleAssignmentPageUrlPattern);
  });

  describe('RuleAssignment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ruleAssignmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RuleAssignment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/rule-assignment/new$'));
        cy.getEntityCreateUpdateHeading('RuleAssignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ruleAssignmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rule-assignments',
          body: ruleAssignmentSample,
        }).then(({ body }) => {
          ruleAssignment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rule-assignments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/rule-assignments?page=0&size=20>; rel="last",<http://localhost/api/rule-assignments?page=0&size=20>; rel="first"',
              },
              body: [ruleAssignment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ruleAssignmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RuleAssignment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ruleAssignment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ruleAssignmentPageUrlPattern);
      });

      it('edit button click should load edit RuleAssignment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RuleAssignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ruleAssignmentPageUrlPattern);
      });

      it.skip('edit button click should load edit RuleAssignment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RuleAssignment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ruleAssignmentPageUrlPattern);
      });

      it('last delete button click should delete instance of RuleAssignment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ruleAssignment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ruleAssignmentPageUrlPattern);

        ruleAssignment = undefined;
      });
    });
  });

  describe('new RuleAssignment page', () => {
    beforeEach(() => {
      cy.visit(`${ruleAssignmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RuleAssignment');
    });

    it('should create an instance of RuleAssignment', () => {
      cy.get(`[data-cy="entityName"]`).type('likewise');
      cy.get(`[data-cy="entityName"]`).should('have.value', 'likewise');

      cy.get(`[data-cy="entityId"]`).type('2206');
      cy.get(`[data-cy="entityId"]`).should('have.value', '2206');

      cy.get(`[data-cy="createdBy"]`).type('underestimate apropos gosh');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'underestimate apropos gosh');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-24T06:29');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-24T06:29');

      cy.get(`[data-cy="lastModifiedBy"]`).type('alarmed abaft but');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'alarmed abaft but');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T09:18');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T09:18');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ruleAssignment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', ruleAssignmentPageUrlPattern);
    });
  });
});
