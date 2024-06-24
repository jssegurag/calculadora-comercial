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

describe('DroolsRuleFile e2e test', () => {
  const droolsRuleFilePageUrl = '/drools-rule-file';
  const droolsRuleFilePageUrlPattern = new RegExp('/drools-rule-file(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const droolsRuleFileSample = { fileName: 'once', fileContent: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', active: true };

  let droolsRuleFile;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/drools-rule-files+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/drools-rule-files').as('postEntityRequest');
    cy.intercept('DELETE', '/api/drools-rule-files/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (droolsRuleFile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/drools-rule-files/${droolsRuleFile.id}`,
      }).then(() => {
        droolsRuleFile = undefined;
      });
    }
  });

  it('DroolsRuleFiles menu should load DroolsRuleFiles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('drools-rule-file');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DroolsRuleFile').should('exist');
    cy.url().should('match', droolsRuleFilePageUrlPattern);
  });

  describe('DroolsRuleFile page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(droolsRuleFilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DroolsRuleFile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/drools-rule-file/new$'));
        cy.getEntityCreateUpdateHeading('DroolsRuleFile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', droolsRuleFilePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/drools-rule-files',
          body: droolsRuleFileSample,
        }).then(({ body }) => {
          droolsRuleFile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/drools-rule-files+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/drools-rule-files?page=0&size=20>; rel="last",<http://localhost/api/drools-rule-files?page=0&size=20>; rel="first"',
              },
              body: [droolsRuleFile],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(droolsRuleFilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DroolsRuleFile page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('droolsRuleFile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', droolsRuleFilePageUrlPattern);
      });

      it('edit button click should load edit DroolsRuleFile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DroolsRuleFile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', droolsRuleFilePageUrlPattern);
      });

      it.skip('edit button click should load edit DroolsRuleFile page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DroolsRuleFile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', droolsRuleFilePageUrlPattern);
      });

      it('last delete button click should delete instance of DroolsRuleFile', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('droolsRuleFile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', droolsRuleFilePageUrlPattern);

        droolsRuleFile = undefined;
      });
    });
  });

  describe('new DroolsRuleFile page', () => {
    beforeEach(() => {
      cy.visit(`${droolsRuleFilePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DroolsRuleFile');
    });

    it('should create an instance of DroolsRuleFile', () => {
      cy.get(`[data-cy="fileName"]`).type('since');
      cy.get(`[data-cy="fileName"]`).should('have.value', 'since');

      cy.get(`[data-cy="fileContent"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="fileContent"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="description"]`).type('oof');
      cy.get(`[data-cy="description"]`).should('have.value', 'oof');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="createdBy"]`).type('and rosy');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'and rosy');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-23T22:22');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-23T22:22');

      cy.get(`[data-cy="lastModifiedBy"]`).type('evenly studio');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'evenly studio');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-24T04:05');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-24T04:05');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        droolsRuleFile = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', droolsRuleFilePageUrlPattern);
    });
  });
});
