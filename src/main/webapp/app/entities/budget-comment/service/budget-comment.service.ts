import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBudgetComment, NewBudgetComment } from '../budget-comment.model';

export type PartialUpdateBudgetComment = Partial<IBudgetComment> & Pick<IBudgetComment, 'id'>;

type RestOf<T extends IBudgetComment | NewBudgetComment> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBudgetComment = RestOf<IBudgetComment>;

export type NewRestBudgetComment = RestOf<NewBudgetComment>;

export type PartialUpdateRestBudgetComment = RestOf<PartialUpdateBudgetComment>;

export type EntityResponseType = HttpResponse<IBudgetComment>;
export type EntityArrayResponseType = HttpResponse<IBudgetComment[]>;

@Injectable({ providedIn: 'root' })
export class BudgetCommentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/budget-comments');

  create(budgetComment: NewBudgetComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetComment);
    return this.http
      .post<RestBudgetComment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(budgetComment: IBudgetComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetComment);
    return this.http
      .put<RestBudgetComment>(`${this.resourceUrl}/${this.getBudgetCommentIdentifier(budgetComment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(budgetComment: PartialUpdateBudgetComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(budgetComment);
    return this.http
      .patch<RestBudgetComment>(`${this.resourceUrl}/${this.getBudgetCommentIdentifier(budgetComment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBudgetComment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBudgetComment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBudgetCommentIdentifier(budgetComment: Pick<IBudgetComment, 'id'>): number {
    return budgetComment.id;
  }

  compareBudgetComment(o1: Pick<IBudgetComment, 'id'> | null, o2: Pick<IBudgetComment, 'id'> | null): boolean {
    return o1 && o2 ? this.getBudgetCommentIdentifier(o1) === this.getBudgetCommentIdentifier(o2) : o1 === o2;
  }

  addBudgetCommentToCollectionIfMissing<Type extends Pick<IBudgetComment, 'id'>>(
    budgetCommentCollection: Type[],
    ...budgetCommentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const budgetComments: Type[] = budgetCommentsToCheck.filter(isPresent);
    if (budgetComments.length > 0) {
      const budgetCommentCollectionIdentifiers = budgetCommentCollection.map(budgetCommentItem =>
        this.getBudgetCommentIdentifier(budgetCommentItem),
      );
      const budgetCommentsToAdd = budgetComments.filter(budgetCommentItem => {
        const budgetCommentIdentifier = this.getBudgetCommentIdentifier(budgetCommentItem);
        if (budgetCommentCollectionIdentifiers.includes(budgetCommentIdentifier)) {
          return false;
        }
        budgetCommentCollectionIdentifiers.push(budgetCommentIdentifier);
        return true;
      });
      return [...budgetCommentsToAdd, ...budgetCommentCollection];
    }
    return budgetCommentCollection;
  }

  protected convertDateFromClient<T extends IBudgetComment | NewBudgetComment | PartialUpdateBudgetComment>(budgetComment: T): RestOf<T> {
    return {
      ...budgetComment,
      createdDate: budgetComment.createdDate?.toJSON() ?? null,
      lastModifiedDate: budgetComment.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBudgetComment: RestBudgetComment): IBudgetComment {
    return {
      ...restBudgetComment,
      createdDate: restBudgetComment.createdDate ? dayjs(restBudgetComment.createdDate) : undefined,
      lastModifiedDate: restBudgetComment.lastModifiedDate ? dayjs(restBudgetComment.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBudgetComment>): HttpResponse<IBudgetComment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBudgetComment[]>): HttpResponse<IBudgetComment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
