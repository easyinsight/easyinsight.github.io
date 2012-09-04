package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 4/16/12
 * Time: 2:44 PM
 */
public class BatchbookSuperTagSource extends BatchbookBaseSource {

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_SUPER_TAG;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fieldList = new ArrayList<AnalysisItem>();
        try {
            BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
            String recordIDName = getFeedName() + " Record ID";
            Key recordKey = keys.get(recordIDName);
            if (recordKey == null) {
                recordKey = new NamedKey(recordIDName);
            }
            keys.put(recordIDName, recordKey);
            fieldList.add(new AnalysisDimension(recordKey));
            //String encodedName = URLEncoder.encode(getFeedName(), "UTF-8");
            String encodedName = getFeedName().replace(" ", "%20");
            System.out.println("/service/super_tags/" + encodedName + ".xml");
            Document doc = runRestRequest("/service/super_tags/" + encodedName + ".xml", httpClient, new Builder(), batchbookCompositeSource.getUrl(), batchbookCompositeSource);
            Node node = doc.query("/super_tag").get(0);
            String superTagName = queryField(node, "name/text()");
            Nodes fields = node.query("fields/field");
            for (int i = 0; i < fields.size(); i++) {
                Element fieldNode = (Element) fields.get(i);
                String fieldType = fieldNode.getAttribute("type").getValue();
                String fieldName;
                Attribute fieldAttribute = fieldNode.getAttribute("name");
                if (fieldAttribute == null) {
                    fieldName = queryField(fieldNode, "text()");
                } else {
                    fieldName = fieldAttribute.getValue();
                }
                AnalysisDimension multipleChoiceDimension = new AnalysisDimension();
                Key key = keys.get(fieldName);
                if (key == null) {
                    key = new NamedKey(fieldName);
                }
                multipleChoiceDimension.setKey(key);
                fieldList.add(multipleChoiceDimension);
            }
        } catch (ReportException re) {
            //throw re;
        } catch (Exception e) {
            // ignore, might be an old super tag
        }
        return fieldList;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        List<Map<String, String>> list = batchbookCompositeSource.getSuperTagMap().get(getFeedName());
        if (list != null) {
            for (Map<String, String> map : list) {
                IRow row = dataSet.createRow();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Key key;
                    if (entry.getKey().equals("SuperTagRecordID")) {
                        key = keys.get(getFeedName() + " Record ID");
                    } else {
                        key = keys.get(entry.getKey());
                    }
                    row.addValue(key, entry.getValue());
                }
            }
        }
        return dataSet;
    }

    /*@Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM BATCHBOOK_SUPERTAG WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO BATCHBOOK_SUPERTAG (DATA_SOURCE_ID, SUPERTAG_ID) VALUES (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setLong(2, superTagID);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SUPERTAG_ID FROM BATCHBOOK_SUPERTAG WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        superTagID = rs.getLong(1);
    }*/
}
