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
@WebService(name="EITransactionalLoad", portName = "EITransactionalLoadPort", serviceName = "EITransactionalLoad")
public interface ITransactionalLoadAPI {
    public boolean validateCredentials();

    public String beginTransaction(@WebParam(name="dataSourceName") String dataSourceName,
                                   @WebParam(name="transactionOperation") boolean replaceData,
                                   @WebParam(name = "changeDataSourceToMatch") boolean changeDataSourceToMatch);

    public CommitResult commit(@WebParam(name="transactionID") String transactionID);

    public void rollback(@WebParam(name="transactionID") String transactionID);

    void loadRows(@WebParam(name="rows") Row[] rows, @WebParam(name="transactionID") String transactionID);    
}