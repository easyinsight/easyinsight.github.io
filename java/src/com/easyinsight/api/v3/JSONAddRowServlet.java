package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/21/14
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class JSONAddRowServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        DataStorage dataStorage = null;
        try {
            JSONObject returnObject = new JSONObject();
            JSONArray errors = new JSONArray();
            DateFormat standardDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String dataSourceKey = request.getParameter("dataSourceID");

            String action = (String) jsonObject.get("action");
            JSONArray rows = (JSONArray) jsonObject.get("rows");
            boolean useDateFormats = Boolean.valueOf(String.valueOf(jsonObject.get("custom_date_formats")));
            long dataSourceID = new FeedStorage().dataSourceIDForDataSource(dataSourceKey);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            Map<String, AnalysisItem> fieldMap = new HashMap<String, AnalysisItem>();
            for (AnalysisItem field : dataSource.getFields()) {
                fieldMap.put(field.getKey().toKeyString().toLowerCase(), field);
            }
            DataSet ds = new DataSet();
            for (Object o : rows) {
                JSONObject row = (JSONObject) o;
                IRow curRow = ds.createRow();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    AnalysisItem analysisItem = fieldMap.get(entry.getKey().toLowerCase());
                    if (analysisItem == null) {
                        JSONObject err = new JSONObject();
                        err.put("row", row);
                        err.put("problem", entry.getKey() + " is not a valid field for the data source.");
                        errors.add(err);
                    } else {
                        Object val = entry.getValue();
                        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            if (val instanceof Number) {
                                curRow.addValue(analysisItem.getKey(), new Date(((Number) val).longValue()));
                            } else if ("".equals(val)) {
                                curRow.addValue(analysisItem.getKey(), new EmptyValue());
                            } else {
                                DateFormat df;
                                if (useDateFormats) {
                                    df = new SimpleDateFormat(((AnalysisDateDimension) analysisItem).getCustomDateFormat());
                                } else {
                                    df = standardDateFormat;
                                }
                                try {
                                    curRow.addValue(analysisItem.getKey(), df.parse((String) val));
                                } catch(ParseException ex) {
                                    JSONObject err = new JSONObject();
                                    err.put("row", row);
                                    err.put("problem", "Can't parse the date for " + entry.getKey());
                                    errors.add(err);
                                }
                            }
                        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                            if (val instanceof Number)
                                curRow.addValue(analysisItem.getKey(), ((Number) val).doubleValue());
                            else
                                curRow.addValue(analysisItem.getKey(), Double.parseDouble((String) val));
                        } else {
                            curRow.addValue(analysisItem.getKey(), val.toString());
                        }
                    }
                }
            }

            if (errors.size() > 0) {
                returnObject.put("errors", errors);
            } else {
                dataStorage = DataStorage.writeConnection(dataSource, conn, SecurityUtil.getAccountID());

                if ("add".equals(action) || ((action == null || "".equals(action)) && "post".equals(request.getMethod().toLowerCase()))) {
                    dataStorage.insertData(ds);
                    dataStorage.commit();
                } else if ("replace".equals(action) || ((action == null || "".equals(action)) && "put".equals(request.getMethod().toLowerCase()))) {
                    dataStorage.truncate();
                    dataStorage.insertData(ds);
                    dataStorage.commit();
                }
                PreparedStatement updateSourceStmt = conn.prepareStatement("UPDATE DATA_FEED SET LAST_REFRESH_START = ? WHERE DATA_FEED_ID = ?");
                updateSourceStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                updateSourceStmt.setLong(2, dataSource.getDataFeedID());
                updateSourceStmt.executeUpdate();
                updateSourceStmt.close();
            }




            return new ResponseInfo(returnObject.containsKey("errors") ? ResponseInfo.SERVER_ERROR : ResponseInfo.ALL_GOOD, returnObject.toString());
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }


    }
}
