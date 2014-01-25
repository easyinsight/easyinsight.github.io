package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import nu.xom.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * User: jamesboe
 * Date: 10/3/13
 * Time: 9:01 AM
 */
public class GetDataSourceServlet extends JSONServlet {
    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject document, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        String key = request.getParameter("dataSourceKey");
        FeedResponse feedResponse = new FeedService().openFeedIfPossible(key);
        if (feedResponse.getStatus() == FeedResponse.SUCCESS) {
            long id = feedResponse.getFeedDescriptor().getId();
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(id, conn);

            jsonObject.put("dataSourceName", dataSource.getFeedName());
            /*if (dataSource instanceof CompositeFeedDefinition) {
                CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) dataSource;
                List<CompositeFeedNode> nodes = compositeFeedDefinition.getCompositeFeedNodes();
            }*/
            JSONArray fields = new JSONArray();
            for (AnalysisItem field : dataSource.getFields()) {
                JSONObject fieldObject = new JSONObject();
                fieldObject.put("key", field.getKey().toKeyString());
                fieldObject.put("name", field.toDisplay());
                String type;
                if (field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    type = "Date";
                } else if (field.hasType(AnalysisItemTypes.MEASURE)) {
                    type = "Measure";
                } else {
                    type = "Grouping";
                }
                fieldObject.put("type", type);
                fields.put(fieldObject);
            }
            jsonObject.put("fields", fields);
            return new ResponseInfo(ResponseInfo.ALL_GOOD, jsonObject.toString());
        } else {
            return new ResponseInfo(ResponseInfo.UNAUTHORIZED, "");
        }
    }
}
