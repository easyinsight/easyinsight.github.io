package com.easyinsight.api.v2;

import com.easyinsight.api.Row;
import com.easyinsight.api.Where;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * User: jamesboe
 * Date: Jun 21, 2010
 * Time: 12:15:12 PM
 */
@WebService(name="EIDataV2", portName = "EIDataV2Port", serviceName = "EIDataV2")
public interface IEIV2API {
    public boolean validateCredentials();

    public void replaceRows(@WebParam(name="dataSourceName") String dataSourceName,
                            @WebParam(name="rows") Row[] rows,
                            @WebParam(name="changeDataSourceToMatch") boolean changeDataSourceToMatch);

    void addRow(@WebParam(name="dataSourceName") String dataSourceName, @WebParam(name="row") Row row,
                @WebParam(name="changeDataSourceToMatch") boolean changeDataSourceToMatch);

    void addRows(@WebParam(name="dataSourceName") String dataSourceName,
                 @WebParam(name="rows") Row[] rows,
                 @WebParam(name="changeDataSourceToMatch") boolean changeDataSourceToMatch);

    void updateRow(@WebParam(name="dataSourceName") String dataSourceName,
                   @WebParam(name="row") Row row, @WebParam(name="where") Where where,
                   @WebParam(name="changeDataSourceToMatch") boolean changeDataSourceToMatch);

    void updateRows(@WebParam(name="dataSourceName") String dataSourceName,
                    @WebParam(name="rows") Row[] rows, @WebParam(name="where") Where where,
                    @WebParam(name="changeDataSourceToMatch") boolean changeDataSourceToMatch);

    void deleteRows(@WebParam(name="dataSourceName") String dataSourceName, @WebParam(name="where") Where where);

    DataSourceInfo getSourceInfo(@WebParam(name="dataSourceName") String dataSourceName);
}
