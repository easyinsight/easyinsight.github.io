package com.easyinsight.api.v3;


import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import nu.xom.Document;
import nu.xom.Nodes;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class BeginTransactionServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        Nodes dataSourceNameNodes = document.query("/beginTransaction/dataSourceName/text()");
        String dataSourceName;
        if (dataSourceNameNodes.size() == 0) {
            return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>You need to specify a data source name.</message>");
        } else {
            dataSourceName = dataSourceNameNodes.get(0).getValue();
        }
        Nodes operationNodes = document.query("/beginTransaction/operation/text()");
        String operation;
        if (operationNodes.size() == 0) {
            return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>You need to specify an operation, either add or replace.</message>");
        } else {
            operation = operationNodes.get(0).getValue();
        }
        String txnString = RandomTextGenerator.generateText(15);
        PreparedStatement insertTxnStmt = conn.prepareStatement("INSERT INTO DATA_TRANSACTION (USER_ID, external_txn_id, txn_date, txn_status, data_source_name," +
                "replace_data, change_data_source_to_match) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)");
        insertTxnStmt.setLong(1, SecurityUtil.getUserID());
        insertTxnStmt.setString(2, txnString);
        insertTxnStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        insertTxnStmt.setInt(4, EIV3API.TRANSACTION_OPENED);
        insertTxnStmt.setString(5, dataSourceName);
        insertTxnStmt.setBoolean(6, "replace".equals(operation));
        insertTxnStmt.setBoolean(7, false);
        insertTxnStmt.execute();
        conn.commit();
        return new ResponseInfo(ResponseInfo.ALL_GOOD, "<transactionID>"+txnString+"</transactionID>");
    }
}
