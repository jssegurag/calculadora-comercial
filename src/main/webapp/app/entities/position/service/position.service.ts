import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPosition, NewPosition } from '../position.model';

export type PartialUpdatePosition = Partial<IPosition> & Pick<IPosition, 'id'>;

type RestOf<T extends IPosition | NewPosition> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestPosition = RestOf<IPosition>;

export type NewRestPosition = RestOf<NewPosition>;

export type PartialUpdateRestPosition = RestOf<PartialUpdatePosition>;

export type EntityResponseType = HttpResponse<IPosition>;
export type EntityArrayResponseType = HttpResponse<IPosition[]>;

@Injectable({ providedIn: 'root' })
export class PositionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/positions');

  create(position: NewPosition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(position);
    return this.http
      .post<RestPosition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(position: IPosition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(position);
    return this.http
      .put<RestPosition>(`${this.resourceUrl}/${this.getPositionIdentifier(position)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(position: PartialUpdatePosition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(position);
    return this.http
      .patch<RestPosition>(`${this.resourceUrl}/${this.getPositionIdentifier(position)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPosition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPosition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPositionIdentifier(position: Pick<IPosition, 'id'>): number {
    return position.id;
  }

  comparePosition(o1: Pick<IPosition, 'id'> | null, o2: Pick<IPosition, 'id'> | null): boolean {
    return o1 && o2 ? this.getPositionIdentifier(o1) === this.getPositionIdentifier(o2) : o1 === o2;
  }

  addPositionToCollectionIfMissing<Type extends Pick<IPosition, 'id'>>(
    positionCollection: Type[],
    ...positionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const positions: Type[] = positionsToCheck.filter(isPresent);
    if (positions.length > 0) {
      const positionCollectionIdentifiers = positionCollection.map(positionItem => this.getPositionIdentifier(positionItem));
      const positionsToAdd = positions.filter(positionItem => {
        const positionIdentifier = this.getPositionIdentifier(positionItem);
        if (positionCollectionIdentifiers.includes(positionIdentifier)) {
          return false;
        }
        positionCollectionIdentifiers.push(positionIdentifier);
        return true;
      });
      return [...positionsToAdd, ...positionCollection];
    }
    return positionCollection;
  }

  protected convertDateFromClient<T extends IPosition | NewPosition | PartialUpdatePosition>(position: T): RestOf<T> {
    return {
      ...position,
      createdDate: position.createdDate?.toJSON() ?? null,
      lastModifiedDate: position.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPosition: RestPosition): IPosition {
    return {
      ...restPosition,
      createdDate: restPosition.createdDate ? dayjs(restPosition.createdDate) : undefined,
      lastModifiedDate: restPosition.lastModifiedDate ? dayjs(restPosition.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPosition>): HttpResponse<IPosition> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPosition[]>): HttpResponse<IPosition[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
