package com.easyinsight.api.basicauth;

import com.easyinsight.api.Row;
import com.easyinsight.api.Where;

import javax.jws.WebService;
import javax.jws.WebParam;

/**
 * User: James Boe
 * Date: Feb 3, 2009
 * Time: 9:23:15 PM
 */
@WebService(name="BasicAuthValidatedPublish", portName = "BasicAuthValidatedPublishPort", serviceName = "BasicAuthValidatedPublish")
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
