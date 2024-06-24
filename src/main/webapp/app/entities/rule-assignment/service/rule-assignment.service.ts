import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRuleAssignment, NewRuleAssignment } from '../rule-assignment.model';

export type PartialUpdateRuleAssignment = Partial<IRuleAssignment> & Pick<IRuleAssignment, 'id'>;

type RestOf<T extends IRuleAssignment | NewRuleAssignment> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestRuleAssignment = RestOf<IRuleAssignment>;

export type NewRestRuleAssignment = RestOf<NewRuleAssignment>;

export type PartialUpdateRestRuleAssignment = RestOf<PartialUpdateRuleAssignment>;

export type EntityResponseType = HttpResponse<IRuleAssignment>;
export type EntityArrayResponseType = HttpResponse<IRuleAssignment[]>;

@Injectable({ providedIn: 'root' })
export class RuleAssignmentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rule-assignments');

  create(ruleAssignment: NewRuleAssignment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ruleAssignment);
    return this.http
      .post<RestRuleAssignment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ruleAssignment: IRuleAssignment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ruleAssignment);
    return this.http
      .put<RestRuleAssignment>(`${this.resourceUrl}/${this.getRuleAssignmentIdentifier(ruleAssignment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ruleAssignment: PartialUpdateRuleAssignment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ruleAssignment);
    return this.http
      .patch<RestRuleAssignment>(`${this.resourceUrl}/${this.getRuleAssignmentIdentifier(ruleAssignment)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRuleAssignment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRuleAssignment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRuleAssignmentIdentifier(ruleAssignment: Pick<IRuleAssignment, 'id'>): number {
    return ruleAssignment.id;
  }

  compareRuleAssignment(o1: Pick<IRuleAssignment, 'id'> | null, o2: Pick<IRuleAssignment, 'id'> | null): boolean {
    return o1 && o2 ? this.getRuleAssignmentIdentifier(o1) === this.getRuleAssignmentIdentifier(o2) : o1 === o2;
  }

  addRuleAssignmentToCollectionIfMissing<Type extends Pick<IRuleAssignment, 'id'>>(
    ruleAssignmentCollection: Type[],
    ...ruleAssignmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ruleAssignments: Type[] = ruleAssignmentsToCheck.filter(isPresent);
    if (ruleAssignments.length > 0) {
      const ruleAssignmentCollectionIdentifiers = ruleAssignmentCollection.map(ruleAssignmentItem =>
        this.getRuleAssignmentIdentifier(ruleAssignmentItem),
      );
      const ruleAssignmentsToAdd = ruleAssignments.filter(ruleAssignmentItem => {
        const ruleAssignmentIdentifier = this.getRuleAssignmentIdentifier(ruleAssignmentItem);
        if (ruleAssignmentCollectionIdentifiers.includes(ruleAssignmentIdentifier)) {
          return false;
        }
        ruleAssignmentCollectionIdentifiers.push(ruleAssignmentIdentifier);
        return true;
      });
      return [...ruleAssignmentsToAdd, ...ruleAssignmentCollection];
    }
    return ruleAssignmentCollection;
  }

  protected convertDateFromClient<T extends IRuleAssignment | NewRuleAssignment | PartialUpdateRuleAssignment>(
    ruleAssignment: T,
  ): RestOf<T> {
    return {
      ...ruleAssignment,
      createdDate: ruleAssignment.createdDate?.toJSON() ?? null,
      lastModifiedDate: ruleAssignment.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRuleAssignment: RestRuleAssignment): IRuleAssignment {
    return {
      ...restRuleAssignment,
      createdDate: restRuleAssignment.createdDate ? dayjs(restRuleAssignment.createdDate) : undefined,
      lastModifiedDate: restRuleAssignment.lastModifiedDate ? dayjs(restRuleAssignment.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRuleAssignment>): HttpResponse<IRuleAssignment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRuleAssignment[]>): HttpResponse<IRuleAssignment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
