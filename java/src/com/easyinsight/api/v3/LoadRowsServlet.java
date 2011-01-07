package com.easyinsight.api.v3;


import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.api.v2.SerializedLoad;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import nu.xom.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class LoadRowsServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn) throws Exception {
        Nodes baseRows = document.query("/rows");
        if (baseRows.size() == 0) {
            throw new ServiceRuntimeException("The root element of the request needs to be rows.");
        }
        Element rootNode = (Element) baseRows.get(0);
        Attribute dataSourceAttribute = rootNode.getAttribute("transactionID");
        if (dataSourceAttribute == null) {
            throw new ServiceRuntimeException("You need to specify a transaction ID via the transactionID attribute on the root element of the request.");
        }
        String transactionID = dataSourceAttribute.getValue();
        PreparedStatement txnQueryStmt = conn.prepareStatement("SELECT data_transaction_id, data_source_name FROM data_transaction where external_txn_id = ? AND user_id = ?");
        txnQueryStmt.setString(1, transactionID);
        txnQueryStmt.setLong(2, SecurityUtil.getUserID());
        ResultSet rs = txnQueryStmt.executeQuery();
        rs.next();
        long transactionDBID = rs.getLong(1);
        String dataSourceKey = rs.getString(2);
        Map<Long, Boolean> dataSources = findDataSourceIDsByName(dataSourceKey, conn);
        if (dataSources.size() == 0) {
            throw new ServiceRuntimeException("We couldn't find a data source by the name or key of " + transactionID + ". Create the data source by posting to defineDataSource before you use this call.");
        } else if (dataSources.size() == 2) {
            throw new ServiceRuntimeException("We found more than one data source by the name of " + transactionID + ". Use a data source key or delete all but one of the data sources by this name from your Easy Insight account.");
        }
        FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSources.keySet().iterator().next(), conn);
        Map<String, AnalysisItem> fieldMap = new HashMap<String, AnalysisItem>();
        for (AnalysisItem field : dataSource.getFields()) {
            fieldMap.put(field.getKey().toKeyString().toLowerCase(), field);
        }
        Nodes rowNodes = document.query("/rows/row");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DataSet dataSet = new DataSet();
        for (int i = 0; i < rowNodes.size(); i++) {
            Node rowNode = rowNodes.get(i);
            IRow row = dataSet.createRow();
            for (int j = 0; j < rowNode.getChildCount(); j++) {
                Node node = rowNode.getChild(j);
                if (node instanceof Element) {
                    Element columnNode = (Element) node;
                    String nodeName = columnNode.getLocalName().toLowerCase();
                    AnalysisItem analysisItem = fieldMap.get(nodeName);
                    if (analysisItem == null) {
                        throw new ServiceRuntimeException("No field was found in the data source definition matching the key of " + nodeName + ".");
                    }
                    String value = columnNode.getValue().trim();
                    if ("".equals(value)) {
                        row.addValue(analysisItem.getKey(), new EmptyValue());
                    } else {
                        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            try {
                                row.addValue(analysisItem.getKey(), dateFormat.parse(value));
                            } catch (ParseException e) {
                                throw new ServiceRuntimeException("We couldn't parse the date value of " + value + " that you passed in with " + nodeName + ". Date values should match the pattern of yyyy-MM-dd'T'HH:mm:ss.");
                            }
                        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                            try {
                                row.addValue(analysisItem.getKey(), Double.parseDouble(value));
                            } catch (NumberFormatException e) {
                                throw new ServiceRuntimeException("We couldn't parse the numeric value of " + value + " that you passed in with " + nodeName + ".");
                            }
                        } else {
                            row.addValue(analysisItem.getKey(), value);
                        }
                    }
                }
            }
        }
        SerializedLoad load = SerializedLoad.fromDataSet(dataSet);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO data_transaction_command (data_transaction_id, command_blob) VALUES (?, ?)");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(load);
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedInputStream bis = new BufferedInputStream(bais, 1024);
        insertStmt.setLong(1, transactionDBID);
        insertStmt.setBinaryStream(2, bis, bytes.length);
        insertStmt.execute();
        return new ResponseInfo(ResponseInfo.ALL_GOOD, "");
    }
}
