package com.easyinsight.api.wsdeathstar;

import com.easyinsight.api.Row;
import com.easyinsight.api.Where;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * User: James Boe
 * Date: Jan 29, 2009
 * Time: 1:41:05 PM
 */
@WebService(name="WSSUncheckedPublish", portName = "WSSUncheckedPublishPort", serviceName = "WSSUncheckedPublish")
public interface IUncheckedPublishService {

    boolean validateCredentials();

    void disableUnchecked(@WebParam(name="dataSourceKey") String dataSourceKey);

    public String replaceRows(@WebParam(name="dataSourceName") String dataSourceName,
                            @WebParam(name="rows") Row[] rows);

    String addRow(@WebParam(name="dataSourceName") String dataSourceName, Row row);

    String addRows(@WebParam(name="dataSourceName") String dataSourceName,
                 @WebParam(name="rows") Row[] rows);

    String updateRow(@WebParam(name="dataSourceName") String dataSourceName,
                   @WebParam(name="row") Row row, @WebParam(name="where") Where where);

    String updateRows(@WebParam(name="dataSourceName") String dataSourceName,
                    @WebParam(name="rows") Row[] rows, @WebParam(name="where") Where where);

    void deleteRows(@WebParam(name="dataSourceName") String dataSourceName, @WebParam(name="where") Where where);
}
