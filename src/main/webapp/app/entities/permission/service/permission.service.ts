import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPermission, NewPermission } from '../permission.model';

export type PartialUpdatePermission = Partial<IPermission> & Pick<IPermission, 'id'>;

type RestOf<T extends IPermission | NewPermission> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestPermission = RestOf<IPermission>;

export type NewRestPermission = RestOf<NewPermission>;

export type PartialUpdateRestPermission = RestOf<PartialUpdatePermission>;

export type EntityResponseType = HttpResponse<IPermission>;
export type EntityArrayResponseType = HttpResponse<IPermission[]>;

@Injectable({ providedIn: 'root' })
export class PermissionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permissions');

  create(permission: NewPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(permission);
    return this.http
      .post<RestPermission>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(permission: IPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(permission);
    return this.http
      .put<RestPermission>(`${this.resourceUrl}/${this.getPermissionIdentifier(permission)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(permission: PartialUpdatePermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(permission);
    return this.http
      .patch<RestPermission>(`${this.resourceUrl}/${this.getPermissionIdentifier(permission)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPermission>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPermission[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPermissionIdentifier(permission: Pick<IPermission, 'id'>): number {
    return permission.id;
  }

  comparePermission(o1: Pick<IPermission, 'id'> | null, o2: Pick<IPermission, 'id'> | null): boolean {
    return o1 && o2 ? this.getPermissionIdentifier(o1) === this.getPermissionIdentifier(o2) : o1 === o2;
  }

  addPermissionToCollectionIfMissing<Type extends Pick<IPermission, 'id'>>(
    permissionCollection: Type[],
    ...permissionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const permissions: Type[] = permissionsToCheck.filter(isPresent);
    if (permissions.length > 0) {
      const permissionCollectionIdentifiers = permissionCollection.map(permissionItem => this.getPermissionIdentifier(permissionItem));
      const permissionsToAdd = permissions.filter(permissionItem => {
        const permissionIdentifier = this.getPermissionIdentifier(permissionItem);
        if (permissionCollectionIdentifiers.includes(permissionIdentifier)) {
          return false;
        }
        permissionCollectionIdentifiers.push(permissionIdentifier);
        return true;
      });
      return [...permissionsToAdd, ...permissionCollection];
    }
    return permissionCollection;
  }

  protected convertDateFromClient<T extends IPermission | NewPermission | PartialUpdatePermission>(permission: T): RestOf<T> {
    return {
      ...permission,
      createdDate: permission.createdDate?.toJSON() ?? null,
      lastModifiedDate: permission.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPermission: RestPermission): IPermission {
    return {
      ...restPermission,
      createdDate: restPermission.createdDate ? dayjs(restPermission.createdDate) : undefined,
      lastModifiedDate: restPermission.lastModifiedDate ? dayjs(restPermission.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPermission>): HttpResponse<IPermission> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPermission[]>): HttpResponse<IPermission[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
