import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDroolsRuleFile, NewDroolsRuleFile } from '../drools-rule-file.model';

export type PartialUpdateDroolsRuleFile = Partial<IDroolsRuleFile> & Pick<IDroolsRuleFile, 'id'>;

type RestOf<T extends IDroolsRuleFile | NewDroolsRuleFile> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestDroolsRuleFile = RestOf<IDroolsRuleFile>;

export type NewRestDroolsRuleFile = RestOf<NewDroolsRuleFile>;

export type PartialUpdateRestDroolsRuleFile = RestOf<PartialUpdateDroolsRuleFile>;

export type EntityResponseType = HttpResponse<IDroolsRuleFile>;
export type EntityArrayResponseType = HttpResponse<IDroolsRuleFile[]>;

@Injectable({ providedIn: 'root' })
export class DroolsRuleFileService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/drools-rule-files');

  create(droolsRuleFile: NewDroolsRuleFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(droolsRuleFile);
    return this.http
      .post<RestDroolsRuleFile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(droolsRuleFile: IDroolsRuleFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(droolsRuleFile);
    return this.http
      .put<RestDroolsRuleFile>(`${this.resourceUrl}/${this.getDroolsRuleFileIdentifier(droolsRuleFile)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(droolsRuleFile: PartialUpdateDroolsRuleFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(droolsRuleFile);
    return this.http
      .patch<RestDroolsRuleFile>(`${this.resourceUrl}/${this.getDroolsRuleFileIdentifier(droolsRuleFile)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDroolsRuleFile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDroolsRuleFile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDroolsRuleFileIdentifier(droolsRuleFile: Pick<IDroolsRuleFile, 'id'>): number {
    return droolsRuleFile.id;
  }

  compareDroolsRuleFile(o1: Pick<IDroolsRuleFile, 'id'> | null, o2: Pick<IDroolsRuleFile, 'id'> | null): boolean {
    return o1 && o2 ? this.getDroolsRuleFileIdentifier(o1) === this.getDroolsRuleFileIdentifier(o2) : o1 === o2;
  }

  addDroolsRuleFileToCollectionIfMissing<Type extends Pick<IDroolsRuleFile, 'id'>>(
    droolsRuleFileCollection: Type[],
    ...droolsRuleFilesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const droolsRuleFiles: Type[] = droolsRuleFilesToCheck.filter(isPresent);
    if (droolsRuleFiles.length > 0) {
      const droolsRuleFileCollectionIdentifiers = droolsRuleFileCollection.map(droolsRuleFileItem =>
        this.getDroolsRuleFileIdentifier(droolsRuleFileItem),
      );
      const droolsRuleFilesToAdd = droolsRuleFiles.filter(droolsRuleFileItem => {
        const droolsRuleFileIdentifier = this.getDroolsRuleFileIdentifier(droolsRuleFileItem);
        if (droolsRuleFileCollectionIdentifiers.includes(droolsRuleFileIdentifier)) {
          return false;
        }
        droolsRuleFileCollectionIdentifiers.push(droolsRuleFileIdentifier);
        return true;
      });
      return [...droolsRuleFilesToAdd, ...droolsRuleFileCollection];
    }
    return droolsRuleFileCollection;
  }

  protected convertDateFromClient<T extends IDroolsRuleFile | NewDroolsRuleFile | PartialUpdateDroolsRuleFile>(
    droolsRuleFile: T,
  ): RestOf<T> {
    return {
      ...droolsRuleFile,
      createdDate: droolsRuleFile.createdDate?.toJSON() ?? null,
      lastModifiedDate: droolsRuleFile.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDroolsRuleFile: RestDroolsRuleFile): IDroolsRuleFile {
    return {
      ...restDroolsRuleFile,
      createdDate: restDroolsRuleFile.createdDate ? dayjs(restDroolsRuleFile.createdDate) : undefined,
      lastModifiedDate: restDroolsRuleFile.lastModifiedDate ? dayjs(restDroolsRuleFile.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDroolsRuleFile>): HttpResponse<IDroolsRuleFile> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDroolsRuleFile[]>): HttpResponse<IDroolsRuleFile[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
