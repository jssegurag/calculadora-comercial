import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFinancialParameterType, NewFinancialParameterType } from '../financial-parameter-type.model';

export type PartialUpdateFinancialParameterType = Partial<IFinancialParameterType> & Pick<IFinancialParameterType, 'id'>;

type RestOf<T extends IFinancialParameterType | NewFinancialParameterType> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestFinancialParameterType = RestOf<IFinancialParameterType>;

export type NewRestFinancialParameterType = RestOf<NewFinancialParameterType>;

export type PartialUpdateRestFinancialParameterType = RestOf<PartialUpdateFinancialParameterType>;

export type EntityResponseType = HttpResponse<IFinancialParameterType>;
export type EntityArrayResponseType = HttpResponse<IFinancialParameterType[]>;

@Injectable({ providedIn: 'root' })
export class FinancialParameterTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/financial-parameter-types');

  create(financialParameterType: NewFinancialParameterType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialParameterType);
    return this.http
      .post<RestFinancialParameterType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(financialParameterType: IFinancialParameterType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialParameterType);
    return this.http
      .put<RestFinancialParameterType>(`${this.resourceUrl}/${this.getFinancialParameterTypeIdentifier(financialParameterType)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(financialParameterType: PartialUpdateFinancialParameterType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialParameterType);
    return this.http
      .patch<RestFinancialParameterType>(`${this.resourceUrl}/${this.getFinancialParameterTypeIdentifier(financialParameterType)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFinancialParameterType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFinancialParameterType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFinancialParameterTypeIdentifier(financialParameterType: Pick<IFinancialParameterType, 'id'>): number {
    return financialParameterType.id;
  }

  compareFinancialParameterType(o1: Pick<IFinancialParameterType, 'id'> | null, o2: Pick<IFinancialParameterType, 'id'> | null): boolean {
    return o1 && o2 ? this.getFinancialParameterTypeIdentifier(o1) === this.getFinancialParameterTypeIdentifier(o2) : o1 === o2;
  }

  addFinancialParameterTypeToCollectionIfMissing<Type extends Pick<IFinancialParameterType, 'id'>>(
    financialParameterTypeCollection: Type[],
    ...financialParameterTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const financialParameterTypes: Type[] = financialParameterTypesToCheck.filter(isPresent);
    if (financialParameterTypes.length > 0) {
      const financialParameterTypeCollectionIdentifiers = financialParameterTypeCollection.map(financialParameterTypeItem =>
        this.getFinancialParameterTypeIdentifier(financialParameterTypeItem),
      );
      const financialParameterTypesToAdd = financialParameterTypes.filter(financialParameterTypeItem => {
        const financialParameterTypeIdentifier = this.getFinancialParameterTypeIdentifier(financialParameterTypeItem);
        if (financialParameterTypeCollectionIdentifiers.includes(financialParameterTypeIdentifier)) {
          return false;
        }
        financialParameterTypeCollectionIdentifiers.push(financialParameterTypeIdentifier);
        return true;
      });
      return [...financialParameterTypesToAdd, ...financialParameterTypeCollection];
    }
    return financialParameterTypeCollection;
  }

  protected convertDateFromClient<T extends IFinancialParameterType | NewFinancialParameterType | PartialUpdateFinancialParameterType>(
    financialParameterType: T,
  ): RestOf<T> {
    return {
      ...financialParameterType,
      createdDate: financialParameterType.createdDate?.toJSON() ?? null,
      lastModifiedDate: financialParameterType.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFinancialParameterType: RestFinancialParameterType): IFinancialParameterType {
    return {
      ...restFinancialParameterType,
      createdDate: restFinancialParameterType.createdDate ? dayjs(restFinancialParameterType.createdDate) : undefined,
      lastModifiedDate: restFinancialParameterType.lastModifiedDate ? dayjs(restFinancialParameterType.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFinancialParameterType>): HttpResponse<IFinancialParameterType> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFinancialParameterType[]>): HttpResponse<IFinancialParameterType[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
