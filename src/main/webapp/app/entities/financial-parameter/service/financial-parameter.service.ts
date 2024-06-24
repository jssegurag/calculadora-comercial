import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFinancialParameter, NewFinancialParameter } from '../financial-parameter.model';

export type PartialUpdateFinancialParameter = Partial<IFinancialParameter> & Pick<IFinancialParameter, 'id'>;

type RestOf<T extends IFinancialParameter | NewFinancialParameter> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestFinancialParameter = RestOf<IFinancialParameter>;

export type NewRestFinancialParameter = RestOf<NewFinancialParameter>;

export type PartialUpdateRestFinancialParameter = RestOf<PartialUpdateFinancialParameter>;

export type EntityResponseType = HttpResponse<IFinancialParameter>;
export type EntityArrayResponseType = HttpResponse<IFinancialParameter[]>;

@Injectable({ providedIn: 'root' })
export class FinancialParameterService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/financial-parameters');

  create(financialParameter: NewFinancialParameter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialParameter);
    return this.http
      .post<RestFinancialParameter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(financialParameter: IFinancialParameter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialParameter);
    return this.http
      .put<RestFinancialParameter>(`${this.resourceUrl}/${this.getFinancialParameterIdentifier(financialParameter)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(financialParameter: PartialUpdateFinancialParameter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialParameter);
    return this.http
      .patch<RestFinancialParameter>(`${this.resourceUrl}/${this.getFinancialParameterIdentifier(financialParameter)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFinancialParameter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFinancialParameter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFinancialParameterIdentifier(financialParameter: Pick<IFinancialParameter, 'id'>): number {
    return financialParameter.id;
  }

  compareFinancialParameter(o1: Pick<IFinancialParameter, 'id'> | null, o2: Pick<IFinancialParameter, 'id'> | null): boolean {
    return o1 && o2 ? this.getFinancialParameterIdentifier(o1) === this.getFinancialParameterIdentifier(o2) : o1 === o2;
  }

  addFinancialParameterToCollectionIfMissing<Type extends Pick<IFinancialParameter, 'id'>>(
    financialParameterCollection: Type[],
    ...financialParametersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const financialParameters: Type[] = financialParametersToCheck.filter(isPresent);
    if (financialParameters.length > 0) {
      const financialParameterCollectionIdentifiers = financialParameterCollection.map(financialParameterItem =>
        this.getFinancialParameterIdentifier(financialParameterItem),
      );
      const financialParametersToAdd = financialParameters.filter(financialParameterItem => {
        const financialParameterIdentifier = this.getFinancialParameterIdentifier(financialParameterItem);
        if (financialParameterCollectionIdentifiers.includes(financialParameterIdentifier)) {
          return false;
        }
        financialParameterCollectionIdentifiers.push(financialParameterIdentifier);
        return true;
      });
      return [...financialParametersToAdd, ...financialParameterCollection];
    }
    return financialParameterCollection;
  }

  protected convertDateFromClient<T extends IFinancialParameter | NewFinancialParameter | PartialUpdateFinancialParameter>(
    financialParameter: T,
  ): RestOf<T> {
    return {
      ...financialParameter,
      createdDate: financialParameter.createdDate?.toJSON() ?? null,
      lastModifiedDate: financialParameter.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFinancialParameter: RestFinancialParameter): IFinancialParameter {
    return {
      ...restFinancialParameter,
      createdDate: restFinancialParameter.createdDate ? dayjs(restFinancialParameter.createdDate) : undefined,
      lastModifiedDate: restFinancialParameter.lastModifiedDate ? dayjs(restFinancialParameter.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFinancialParameter>): HttpResponse<IFinancialParameter> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFinancialParameter[]>): HttpResponse<IFinancialParameter[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
