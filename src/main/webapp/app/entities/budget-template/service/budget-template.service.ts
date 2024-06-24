import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBudgetTemplate, NewBudgetTemplate } from '../budget-template.model';

export type PartialUpdateBudgetTemplate = Partial<IBudgetTemplate> & Pick<IBudgetTemplate, 'id'>;

type RestOf<T extends IBudgetTemplate | NewBudgetTemplate> = Omit<T, 'startDate' | 'endDate' | 'createdDate' | 'lastModifiedDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBudgetTemplate = RestOf<IBudgetTemplate>;

export type NewRestBudgetTemplate = RestOf<NewBudgetTemplate>;

export type PartialUpdateRestBudgetTemplate = RestOf<PartialUpdateBudgetTemplate>;

export type EntityResponseType = HttpResponse<IBudgetTemplate>;
export type EntityArrayResponseType = HttpResponse<IBudgetTemplate[]>;

@Injectable({ providedIn: 'root' })
export class BudgetTemplateService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/budget-templates');

  create(budgetTemplate: NewBudgetTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetTemplate);
    return this.http
      .post<RestBudgetTemplate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(budgetTemplate: IBudgetTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetTemplate);
    return this.http
      .put<RestBudgetTemplate>(`${this.resourceUrl}/${this.getBudgetTemplateIdentifier(budgetTemplate)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(budgetTemplate: PartialUpdateBudgetTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetTemplate);
    return this.http
      .patch<RestBudgetTemplate>(`${this.resourceUrl}/${this.getBudgetTemplateIdentifier(budgetTemplate)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBudgetTemplate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBudgetTemplate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBudgetTemplateIdentifier(budgetTemplate: Pick<IBudgetTemplate, 'id'>): number {
    return budgetTemplate.id;
  }

  compareBudgetTemplate(o1: Pick<IBudgetTemplate, 'id'> | null, o2: Pick<IBudgetTemplate, 'id'> | null): boolean {
    return o1 && o2 ? this.getBudgetTemplateIdentifier(o1) === this.getBudgetTemplateIdentifier(o2) : o1 === o2;
  }

  addBudgetTemplateToCollectionIfMissing<Type extends Pick<IBudgetTemplate, 'id'>>(
    budgetTemplateCollection: Type[],
    ...budgetTemplatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const budgetTemplates: Type[] = budgetTemplatesToCheck.filter(isPresent);
    if (budgetTemplates.length > 0) {
      const budgetTemplateCollectionIdentifiers = budgetTemplateCollection.map(budgetTemplateItem =>
        this.getBudgetTemplateIdentifier(budgetTemplateItem),
      );
      const budgetTemplatesToAdd = budgetTemplates.filter(budgetTemplateItem => {
        const budgetTemplateIdentifier = this.getBudgetTemplateIdentifier(budgetTemplateItem);
        if (budgetTemplateCollectionIdentifiers.includes(budgetTemplateIdentifier)) {
          return false;
        }
        budgetTemplateCollectionIdentifiers.push(budgetTemplateIdentifier);
        return true;
      });
      return [...budgetTemplatesToAdd, ...budgetTemplateCollection];
    }
    return budgetTemplateCollection;
  }

  protected convertDateFromClient<T extends IBudgetTemplate | NewBudgetTemplate | PartialUpdateBudgetTemplate>(
    budgetTemplate: T,
  ): RestOf<T> {
    return {
      ...budgetTemplate,
      startDate: budgetTemplate.startDate?.format(DATE_FORMAT) ?? null,
      endDate: budgetTemplate.endDate?.format(DATE_FORMAT) ?? null,
      createdDate: budgetTemplate.createdDate?.toJSON() ?? null,
      lastModifiedDate: budgetTemplate.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBudgetTemplate: RestBudgetTemplate): IBudgetTemplate {
    return {
      ...restBudgetTemplate,
      startDate: restBudgetTemplate.startDate ? dayjs(restBudgetTemplate.startDate) : undefined,
      endDate: restBudgetTemplate.endDate ? dayjs(restBudgetTemplate.endDate) : undefined,
      createdDate: restBudgetTemplate.createdDate ? dayjs(restBudgetTemplate.createdDate) : undefined,
      lastModifiedDate: restBudgetTemplate.lastModifiedDate ? dayjs(restBudgetTemplate.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBudgetTemplate>): HttpResponse<IBudgetTemplate> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBudgetTemplate[]>): HttpResponse<IBudgetTemplate[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
