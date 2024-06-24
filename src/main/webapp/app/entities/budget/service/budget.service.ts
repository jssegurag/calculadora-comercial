import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBudget, NewBudget } from '../budget.model';

export type PartialUpdateBudget = Partial<IBudget> & Pick<IBudget, 'id'>;

type RestOf<T extends IBudget | NewBudget> = Omit<
  T,
  'startDate' | 'endDate' | 'approvalDate' | 'approvalTime' | 'createdDate' | 'lastModifiedDate'
> & {
  startDate?: string | null;
  endDate?: string | null;
  approvalDate?: string | null;
  approvalTime?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBudget = RestOf<IBudget>;

export type NewRestBudget = RestOf<NewBudget>;

export type PartialUpdateRestBudget = RestOf<PartialUpdateBudget>;

export type EntityResponseType = HttpResponse<IBudget>;
export type EntityArrayResponseType = HttpResponse<IBudget[]>;

@Injectable({ providedIn: 'root' })
export class BudgetService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/budgets');

  create(budget: NewBudget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budget);
    return this.http
      .post<RestBudget>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(budget: IBudget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budget);
    return this.http
      .put<RestBudget>(`${this.resourceUrl}/${this.getBudgetIdentifier(budget)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(budget: PartialUpdateBudget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budget);
    return this.http
      .patch<RestBudget>(`${this.resourceUrl}/${this.getBudgetIdentifier(budget)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBudget>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBudget[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBudgetIdentifier(budget: Pick<IBudget, 'id'>): number {
    return budget.id;
  }

  compareBudget(o1: Pick<IBudget, 'id'> | null, o2: Pick<IBudget, 'id'> | null): boolean {
    return o1 && o2 ? this.getBudgetIdentifier(o1) === this.getBudgetIdentifier(o2) : o1 === o2;
  }

  addBudgetToCollectionIfMissing<Type extends Pick<IBudget, 'id'>>(
    budgetCollection: Type[],
    ...budgetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const budgets: Type[] = budgetsToCheck.filter(isPresent);
    if (budgets.length > 0) {
      const budgetCollectionIdentifiers = budgetCollection.map(budgetItem => this.getBudgetIdentifier(budgetItem));
      const budgetsToAdd = budgets.filter(budgetItem => {
        const budgetIdentifier = this.getBudgetIdentifier(budgetItem);
        if (budgetCollectionIdentifiers.includes(budgetIdentifier)) {
          return false;
        }
        budgetCollectionIdentifiers.push(budgetIdentifier);
        return true;
      });
      return [...budgetsToAdd, ...budgetCollection];
    }
    return budgetCollection;
  }

  protected convertDateFromClient<T extends IBudget | NewBudget | PartialUpdateBudget>(budget: T): RestOf<T> {
    return {
      ...budget,
      startDate: budget.startDate?.format(DATE_FORMAT) ?? null,
      endDate: budget.endDate?.format(DATE_FORMAT) ?? null,
      approvalDate: budget.approvalDate?.toJSON() ?? null,
      approvalTime: budget.approvalTime?.toJSON() ?? null,
      createdDate: budget.createdDate?.toJSON() ?? null,
      lastModifiedDate: budget.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBudget: RestBudget): IBudget {
    return {
      ...restBudget,
      startDate: restBudget.startDate ? dayjs(restBudget.startDate) : undefined,
      endDate: restBudget.endDate ? dayjs(restBudget.endDate) : undefined,
      approvalDate: restBudget.approvalDate ? dayjs(restBudget.approvalDate) : undefined,
      approvalTime: restBudget.approvalTime ? dayjs(restBudget.approvalTime) : undefined,
      createdDate: restBudget.createdDate ? dayjs(restBudget.createdDate) : undefined,
      lastModifiedDate: restBudget.lastModifiedDate ? dayjs(restBudget.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBudget>): HttpResponse<IBudget> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBudget[]>): HttpResponse<IBudget[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
