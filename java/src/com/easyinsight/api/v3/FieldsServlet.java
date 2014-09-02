package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedResponse;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/19/14
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class FieldsServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        String dataSourceID = request.getParameter("dataSourceID");
        JSONObject responseObject = new JSONObject();
        FeedResponse feedResponse = new FeedService().openFeedIfPossible(dataSourceID);
        SecurityUtil.authorizeFeed(feedResponse.getFeedDescriptor().getId(), Roles.VIEWER);
        FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(feedResponse.getFeedDescriptor().getId());
        JSONArray fieldList = new JSONArray();
        for(AnalysisItem field : dataSource.getFields()) {
            JSONObject fieldObject = new JSONObject();
            fieldObject.put("name", field.toDisplay());
            fieldObject.put("key", field.getKey().toKeyString());
            String type;
            if(field.hasType(AnalysisItemTypes.MEASURE)) {
                type = "measure";
            } else if(field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                type = "date";
            } else {
                type = "grouping";
            }
            fieldObject.put("type", type);
            fieldList.add(fieldObject);
        }
        responseObject.put("fields", fieldList);

        ResponseInfo r = new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());

        return r;
    }
}
