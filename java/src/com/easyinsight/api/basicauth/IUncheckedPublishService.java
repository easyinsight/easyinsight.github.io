package com.easyinsight.api.basicauth;

import com.easyinsight.api.Row;
import com.easyinsight.api.Where;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * User: James Boe
 * Date: Jan 29, 2009
 * Time: 1:40:56 PM
 */
@WebService(name="UncheckedPublishService", portName = "UncheckedPublishPort")
public interface IUncheckedPublishService {
    public void replaceRows(@WebParam(name="dataSourceName") String dataSourceName,
                            @WebParam(name="rows") Row[] rows);

    void addRow(@WebParam(name="dataSourceName") String dataSourceName, Row row);

    void addRows(@WebParam(name="dataSourceName") String dataSourceName,
                 @WebParam(name="rows") Row[] rows);

    void updateRow(@WebParam(name="dataSourceName") String dataSourceName,
                   @WebParam(name="row") Row row, @WebParam(name="where") Where where);

    void updateRows(@WebParam(name="dataSourceName") String dataSourceName,
                    @WebParam(name="rows") Row[] rows, @WebParam(name="where") Where where);

    void deleteRows(@WebParam(name="dataSourceName") String dataSourceName, @WebParam(name="where") Where where);
}
