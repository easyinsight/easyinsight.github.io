package com.easyinsight.api.v3;

import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.util.ServiceUtil;
import nu.xom.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class CommitServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        DataStorage dataStorage = null;
        try {
            Nodes transactionIDNodes = document.query("/commit/transactionID/text()");
            String transactionID;
            if (transactionIDNodes.size() == 0) {
                return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>You need to specify a transaction ID.</message>");
            } else {
                transactionID = transactionIDNodes.get(0).getValue();
            }
            Nodes callDataIDNodes = document.query("/commit/callDataID/text()");
            String callDataID = null;
            if (callDataIDNodes.size() == 1) {
                callDataID = callDataIDNodes.get(0).getValue();
            }
            PreparedStatement txnQueryStmt = conn.prepareStatement("SELECT data_transaction_id, data_source_name, replace_data, change_data_source_to_match, temp_table_name " +
                        "FROM data_transaction where external_txn_id = ? AND user_id = ?");
            txnQueryStmt.setString(1, transactionID);
            txnQueryStmt.setLong(2, SecurityUtil.getUserID());
            ResultSet rs = txnQueryStmt.executeQuery();
            rs.next();
            //long transactionDatabaseID = rs.getLong(1);
            String dataSourceName = rs.getString(2);
            Map<Long, Boolean> dataSources = findDataSourceIDsByName(dataSourceName, conn);
            if (dataSources.size() == 0) {
                throw new ServiceRuntimeException("We couldn't find a data source by the name or key of " + dataSourceName + ". Create the data source by posting to defineDataSource before you use this call.");
            } else if (dataSources.size() == 2) {
                throw new ServiceRuntimeException("We found more than one data source by the name of " + dataSourceName + ". Use a data source key or delete all but one of the data sources by this name from your Easy Insight account.");
            }
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSources.keySet().iterator().next(), conn);
            boolean replaceData = rs.getBoolean(3);
            String tempTableName = rs.getString(5);
            dataStorage = DataStorage.writeConnection(dataSource, conn, SecurityUtil.getAccountID());
            if (replaceData) {
                dataStorage.truncate();
            }
            dataStorage.insertFromSelect(tempTableName);
            dataStorage.commit();
            conn.commit();
            if (callDataID != null && !"null".equals(callDataID)) {
                try {
                    System.out.println("marking done");
                    ServiceUtil.instance().updateStatus(callDataID, ServiceUtil.DONE);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
            return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }
    }
}
