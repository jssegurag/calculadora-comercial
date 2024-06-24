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

describe('ResourceAllocation e2e test', () => {
  const resourceAllocationPageUrl = '/resource-allocation';
  const resourceAllocationPageUrlPattern = new RegExp('/resource-allocation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const resourceAllocationSample = { assignedHours: 22731.59 };

  let resourceAllocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/resource-allocations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/resource-allocations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/resource-allocations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (resourceAllocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/resource-allocations/${resourceAllocation.id}`,
      }).then(() => {
        resourceAllocation = undefined;
      });
    }
  });

  it('ResourceAllocations menu should load ResourceAllocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('resource-allocation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ResourceAllocation').should('exist');
    cy.url().should('match', resourceAllocationPageUrlPattern);
  });

  describe('ResourceAllocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(resourceAllocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ResourceAllocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/resource-allocation/new$'));
        cy.getEntityCreateUpdateHeading('ResourceAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceAllocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/resource-allocations',
          body: resourceAllocationSample,
        }).then(({ body }) => {
          resourceAllocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/resource-allocations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/resource-allocations?page=0&size=20>; rel="last",<http://localhost/api/resource-allocations?page=0&size=20>; rel="first"',
              },
              body: [resourceAllocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(resourceAllocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ResourceAllocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('resourceAllocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceAllocationPageUrlPattern);
      });

      it('edit button click should load edit ResourceAllocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ResourceAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceAllocationPageUrlPattern);
      });

      it.skip('edit button click should load edit ResourceAllocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ResourceAllocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceAllocationPageUrlPattern);
      });

      it('last delete button click should delete instance of ResourceAllocation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('resourceAllocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceAllocationPageUrlPattern);

        resourceAllocation = undefined;
      });
    });
  });

  describe('new ResourceAllocation page', () => {
    beforeEach(() => {
      cy.visit(`${resourceAllocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ResourceAllocation');
    });

    it('should create an instance of ResourceAllocation', () => {
      cy.get(`[data-cy="assignedHours"]`).type('24937');
      cy.get(`[data-cy="assignedHours"]`).should('have.value', '24937');

      cy.get(`[data-cy="totalCost"]`).type('23216.07');
      cy.get(`[data-cy="totalCost"]`).should('have.value', '23216.07');

      cy.get(`[data-cy="units"]`).type('24812.99');
      cy.get(`[data-cy="units"]`).should('have.value', '24812.99');

      cy.get(`[data-cy="capacity"]`).type('24909.94');
      cy.get(`[data-cy="capacity"]`).should('have.value', '24909.94');

      cy.get(`[data-cy="plannedHours"]`).type('12096.08');
      cy.get(`[data-cy="plannedHours"]`).should('have.value', '12096.08');

      cy.get(`[data-cy="createdBy"]`).type('however scenario');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'however scenario');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-23T19:48');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-23T19:48');

      cy.get(`[data-cy="lastModifiedBy"]`).type('bubbly equal');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'bubbly equal');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-06-23T14:40');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-06-23T14:40');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        resourceAllocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', resourceAllocationPageUrlPattern);
    });
  });
});
