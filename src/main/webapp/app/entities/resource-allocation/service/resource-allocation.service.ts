import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResourceAllocation, NewResourceAllocation } from '../resource-allocation.model';

export type PartialUpdateResourceAllocation = Partial<IResourceAllocation> & Pick<IResourceAllocation, 'id'>;

type RestOf<T extends IResourceAllocation | NewResourceAllocation> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestResourceAllocation = RestOf<IResourceAllocation>;

export type NewRestResourceAllocation = RestOf<NewResourceAllocation>;

export type PartialUpdateRestResourceAllocation = RestOf<PartialUpdateResourceAllocation>;

export type EntityResponseType = HttpResponse<IResourceAllocation>;
export type EntityArrayResponseType = HttpResponse<IResourceAllocation[]>;

@Injectable({ providedIn: 'root' })
export class ResourceAllocationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resource-allocations');

  create(resourceAllocation: NewResourceAllocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resourceAllocation);
    return this.http
      .post<RestResourceAllocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(resourceAllocation: IResourceAllocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resourceAllocation);
    return this.http
      .put<RestResourceAllocation>(`${this.resourceUrl}/${this.getResourceAllocationIdentifier(resourceAllocation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(resourceAllocation: PartialUpdateResourceAllocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resourceAllocation);
    return this.http
      .patch<RestResourceAllocation>(`${this.resourceUrl}/${this.getResourceAllocationIdentifier(resourceAllocation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestResourceAllocation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestResourceAllocation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResourceAllocationIdentifier(resourceAllocation: Pick<IResourceAllocation, 'id'>): number {
    return resourceAllocation.id;
  }

  compareResourceAllocation(o1: Pick<IResourceAllocation, 'id'> | null, o2: Pick<IResourceAllocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getResourceAllocationIdentifier(o1) === this.getResourceAllocationIdentifier(o2) : o1 === o2;
  }

  addResourceAllocationToCollectionIfMissing<Type extends Pick<IResourceAllocation, 'id'>>(
    resourceAllocationCollection: Type[],
    ...resourceAllocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const resourceAllocations: Type[] = resourceAllocationsToCheck.filter(isPresent);
    if (resourceAllocations.length > 0) {
      const resourceAllocationCollectionIdentifiers = resourceAllocationCollection.map(resourceAllocationItem =>
        this.getResourceAllocationIdentifier(resourceAllocationItem),
      );
      const resourceAllocationsToAdd = resourceAllocations.filter(resourceAllocationItem => {
        const resourceAllocationIdentifier = this.getResourceAllocationIdentifier(resourceAllocationItem);
        if (resourceAllocationCollectionIdentifiers.includes(resourceAllocationIdentifier)) {
          return false;
        }
        resourceAllocationCollectionIdentifiers.push(resourceAllocationIdentifier);
        return true;
      });
      return [...resourceAllocationsToAdd, ...resourceAllocationCollection];
    }
    return resourceAllocationCollection;
  }

  protected convertDateFromClient<T extends IResourceAllocation | NewResourceAllocation | PartialUpdateResourceAllocation>(
    resourceAllocation: T,
  ): RestOf<T> {
    return {
      ...resourceAllocation,
      createdDate: resourceAllocation.createdDate?.toJSON() ?? null,
      lastModifiedDate: resourceAllocation.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restResourceAllocation: RestResourceAllocation): IResourceAllocation {
    return {
      ...restResourceAllocation,
      createdDate: restResourceAllocation.createdDate ? dayjs(restResourceAllocation.createdDate) : undefined,
      lastModifiedDate: restResourceAllocation.lastModifiedDate ? dayjs(restResourceAllocation.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestResourceAllocation>): HttpResponse<IResourceAllocation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestResourceAllocation[]>): HttpResponse<IResourceAllocation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
