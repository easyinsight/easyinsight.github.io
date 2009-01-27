package com.easyinsight.api;

import javax.jws.WebService;
import javax.jws.WebParam;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:25:49 PM
 */
@WebService

public interface IPublishService {
    
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
