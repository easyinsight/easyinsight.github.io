package com.easyinsight.api.wsdeathstar;

import com.easyinsight.api.Row;
import com.easyinsight.api.Where;

import javax.jws.WebService;
import javax.jws.WebParam;

/**
 * User: James Boe
 * Date: Feb 3, 2009
 * Time: 9:23:23 PM
 */
@WebService(name="WSSValidatedPublish", portName = "WSSValidatedPublishPort", serviceName = "WSSValidatedPublish")
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
