package com.easyinsight.api.v3;

import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.api.v2.SerializedLoad;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            PreparedStatement txnQueryStmt = conn.prepareStatement("SELECT data_transaction_id, data_source_name, replace_data, change_data_source_to_match " +
                        "FROM data_transaction where external_txn_id = ? AND user_id = ?");
            txnQueryStmt.setString(1, transactionID);
            txnQueryStmt.setLong(2, SecurityUtil.getUserID());
            ResultSet rs = txnQueryStmt.executeQuery();
            rs.next();
            long transactionDatabaseID = rs.getLong(1);
            String dataSourceName = rs.getString(2);
            Map<Long, Boolean> dataSources = findDataSourceIDsByName(dataSourceName, conn);
            if (dataSources.size() == 0) {
                throw new ServiceRuntimeException("We couldn't find a data source by the name or key of " + dataSourceName + ". Create the data source by posting to defineDataSource before you use this call.");
            } else if (dataSources.size() == 2) {
                throw new ServiceRuntimeException("We found more than one data source by the name of " + dataSourceName + ". Use a data source key or delete all but one of the data sources by this name from your Easy Insight account.");
            }
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSources.keySet().iterator().next(), conn);
            boolean replaceData = rs.getBoolean(3);
            DataSet dataSet = loadRows(transactionDatabaseID, conn);
            dataStorage = DataStorage.writeConnection(dataSource, conn, SecurityUtil.getAccountID());
            if (replaceData) {
                dataStorage.truncate();
            }
            dataStorage.insertData(dataSet);
            dataStorage.commit();
            conn.commit();
            return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }
    }

    private DataSet loadRows(long transactionID, EIConnection conn) throws SQLException, IOException, ClassNotFoundException {
        DataSet dataSet = new DataSet();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT COMMAND_BLOB FROM DATA_TRANSACTION_COMMAND WHERE DATA_TRANSACTION_ID = ?", ResultSet.TYPE_FORWARD_ONLY);
        queryStmt.setLong(1, transactionID);
        ResultSet blobRS = queryStmt.executeQuery();
        while (blobRS.next()) {
            byte[] bytes = blobRS.getBytes(1);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            SerializedLoad load = (SerializedLoad) ois.readObject();
            dataSet.getRows().addAll(load.toDataSet().getRows());
        }
        return dataSet;
    }
}
