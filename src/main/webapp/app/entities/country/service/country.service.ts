import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountry, NewCountry } from '../country.model';

export type PartialUpdateCountry = Partial<ICountry> & Pick<ICountry, 'id'>;

type RestOf<T extends ICountry | NewCountry> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestCountry = RestOf<ICountry>;

export type NewRestCountry = RestOf<NewCountry>;

export type PartialUpdateRestCountry = RestOf<PartialUpdateCountry>;

export type EntityResponseType = HttpResponse<ICountry>;
export type EntityArrayResponseType = HttpResponse<ICountry[]>;

@Injectable({ providedIn: 'root' })
export class CountryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/countries');

  create(country: NewCountry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(country);
    return this.http
      .post<RestCountry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(country: ICountry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(country);
    return this.http
      .put<RestCountry>(`${this.resourceUrl}/${this.getCountryIdentifier(country)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(country: PartialUpdateCountry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(country);
    return this.http
      .patch<RestCountry>(`${this.resourceUrl}/${this.getCountryIdentifier(country)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCountry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCountry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCountryIdentifier(country: Pick<ICountry, 'id'>): number {
    return country.id;
  }

  compareCountry(o1: Pick<ICountry, 'id'> | null, o2: Pick<ICountry, 'id'> | null): boolean {
    return o1 && o2 ? this.getCountryIdentifier(o1) === this.getCountryIdentifier(o2) : o1 === o2;
  }

  addCountryToCollectionIfMissing<Type extends Pick<ICountry, 'id'>>(
    countryCollection: Type[],
    ...countriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const countries: Type[] = countriesToCheck.filter(isPresent);
    if (countries.length > 0) {
      const countryCollectionIdentifiers = countryCollection.map(countryItem => this.getCountryIdentifier(countryItem));
      const countriesToAdd = countries.filter(countryItem => {
        const countryIdentifier = this.getCountryIdentifier(countryItem);
        if (countryCollectionIdentifiers.includes(countryIdentifier)) {
          return false;
        }
        countryCollectionIdentifiers.push(countryIdentifier);
        return true;
      });
      return [...countriesToAdd, ...countryCollection];
    }
    return countryCollection;
  }

  protected convertDateFromClient<T extends ICountry | NewCountry | PartialUpdateCountry>(country: T): RestOf<T> {
    return {
      ...country,
      createdDate: country.createdDate?.toJSON() ?? null,
      lastModifiedDate: country.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCountry: RestCountry): ICountry {
    return {
      ...restCountry,
      createdDate: restCountry.createdDate ? dayjs(restCountry.createdDate) : undefined,
      lastModifiedDate: restCountry.lastModifiedDate ? dayjs(restCountry.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCountry>): HttpResponse<ICountry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCountry[]>): HttpResponse<ICountry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
