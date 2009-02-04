package com.easyinsight.api;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * User: James Boe
 * Date: Jan 17, 2009
 * Time: 12:31:33 PM
 */
@WebService(name="ValidatingPublishService")
public interface IValidatingPublishService {

    public boolean validateCredentials();

    void addRow(@WebParam(name="dataSourceKey") String dataSourceName,
                @WebParam(name="row") Row row);

    void addRows(@WebParam(name="dataSourceKey") String dataSourceKey,
                 @WebParam(name="rows")Row[] rows);

    void replaceRows(@WebParam(name="dataSourceKey") String dataSourceKey,
                     @WebParam(name="rows") Row[] rows);

    void updateRow(@WebParam(name="dataSourceKey") String dataSourceKey,
                   @WebParam(name="row") Row row, @WebParam(name="where") Where wheres);

    void updateRows(@WebParam(name="dataSourceKey") String dataSourceKey,
                    @WebParam(name="rows")Row[] rows, @WebParam(name="where") Where wheres);

    void deleteRows(@WebParam(name="dataSourceKey") String dataSourceKey,
                    @WebParam(name="where") Where wheres);
}
