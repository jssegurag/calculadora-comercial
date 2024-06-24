import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserRole, NewUserRole } from '../user-role.model';

export type PartialUpdateUserRole = Partial<IUserRole> & Pick<IUserRole, 'id'>;

type RestOf<T extends IUserRole | NewUserRole> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestUserRole = RestOf<IUserRole>;

export type NewRestUserRole = RestOf<NewUserRole>;

export type PartialUpdateRestUserRole = RestOf<PartialUpdateUserRole>;

export type EntityResponseType = HttpResponse<IUserRole>;
export type EntityArrayResponseType = HttpResponse<IUserRole[]>;

@Injectable({ providedIn: 'root' })
export class UserRoleService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-roles');

  create(userRole: NewUserRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userRole);
    return this.http
      .post<RestUserRole>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userRole: IUserRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userRole);
    return this.http
      .put<RestUserRole>(`${this.resourceUrl}/${this.getUserRoleIdentifier(userRole)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userRole: PartialUpdateUserRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userRole);
    return this.http
      .patch<RestUserRole>(`${this.resourceUrl}/${this.getUserRoleIdentifier(userRole)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserRole>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserRole[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserRoleIdentifier(userRole: Pick<IUserRole, 'id'>): number {
    return userRole.id;
  }

  compareUserRole(o1: Pick<IUserRole, 'id'> | null, o2: Pick<IUserRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserRoleIdentifier(o1) === this.getUserRoleIdentifier(o2) : o1 === o2;
  }

  addUserRoleToCollectionIfMissing<Type extends Pick<IUserRole, 'id'>>(
    userRoleCollection: Type[],
    ...userRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userRoles: Type[] = userRolesToCheck.filter(isPresent);
    if (userRoles.length > 0) {
      const userRoleCollectionIdentifiers = userRoleCollection.map(userRoleItem => this.getUserRoleIdentifier(userRoleItem));
      const userRolesToAdd = userRoles.filter(userRoleItem => {
        const userRoleIdentifier = this.getUserRoleIdentifier(userRoleItem);
        if (userRoleCollectionIdentifiers.includes(userRoleIdentifier)) {
          return false;
        }
        userRoleCollectionIdentifiers.push(userRoleIdentifier);
        return true;
      });
      return [...userRolesToAdd, ...userRoleCollection];
    }
    return userRoleCollection;
  }

  protected convertDateFromClient<T extends IUserRole | NewUserRole | PartialUpdateUserRole>(userRole: T): RestOf<T> {
    return {
      ...userRole,
      createdDate: userRole.createdDate?.toJSON() ?? null,
      lastModifiedDate: userRole.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserRole: RestUserRole): IUserRole {
    return {
      ...restUserRole,
      createdDate: restUserRole.createdDate ? dayjs(restUserRole.createdDate) : undefined,
      lastModifiedDate: restUserRole.lastModifiedDate ? dayjs(restUserRole.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserRole>): HttpResponse<IUserRole> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserRole[]>): HttpResponse<IUserRole[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
