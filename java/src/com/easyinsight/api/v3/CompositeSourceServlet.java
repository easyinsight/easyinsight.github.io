package com.easyinsight.api.v3;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 9/15/14
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompositeSourceServlet extends JSONServlet {
    @Override
    protected ResponseInfo processPost(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject jo = new JSONObject();
        List<CompositeFeedNode> compositeFeedNodeList = ((JSONArray) jsonObject.get("data_sources")).stream().map((a) -> {
            JSONObject j = (JSONObject) a;
            CompositeFeedNode cfn = new CompositeFeedNode();
            FeedResponse fr = new FeedService().openFeedIfPossible(String.valueOf(j.get("url_key")));
            cfn.setDataFeedID(fr.getFeedDescriptor().getId());
            cfn.setDataSourceName(fr.getFeedDescriptor().getName());
            cfn.setDataSourceType(fr.getFeedDescriptor().getDataSourceType());
            cfn.setRefreshBehavior(fr.getFeedDescriptor().getDataSourceBehavior());
            return cfn;
        }
        ).collect(Collectors.toList());

        List<CompositeFeedConnection> compositeFeedConnectionList = ((JSONArray) jsonObject.get("joins")).stream().map((a) -> {
            CompositeFeedConnection cfc = new CompositeFeedConnection();
            try {

                JSONObject j = (JSONObject) a;
                JSONObject source = (JSONObject) j.get("source_ds");
                FeedResponse fr = new FeedService().openFeedIfPossible(String.valueOf(source.get("url_key")));
                cfc.setSourceFeedID(fr.getFeedDescriptor().getId());

                FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(fr.getFeedDescriptor().getId());
                JSONObject sourceField = (JSONObject) j.get("source_field");
                long sourceId = Long.parseLong(String.valueOf(sourceField.get("id")));
                for (AnalysisItem i : dataSource.getFields()) {
                    if(i.getAnalysisItemID() == sourceId) {
                        cfc.setSourceItem(i);
                    }
                }

                JSONObject target = (JSONObject) j.get("target_ds");
                fr = new FeedService().openFeedIfPossible(String.valueOf(target.get("url_key")));
                cfc.setTargetFeedID(fr.getFeedDescriptor().getId());
                dataSource = new FeedStorage().getFeedDefinitionData(fr.getFeedDescriptor().getId());
                JSONObject targetField = (JSONObject) j.get("target_field");
                long targetId = Long.parseLong(String.valueOf(targetField.get("id")));
                for (AnalysisItem i : dataSource.getFields()) {
                    if(i.getAnalysisItemID() == targetId) {
                        cfc.setTargetItem(i);
                    }
                }
                cfc.setSourceCardinality("many".equals(j.get("source_cardinality")) ? IJoin.MANY : IJoin.ONE);
                cfc.setTargetCardinality("many".equals(j.get("target_cardinality")) ? IJoin.MANY : IJoin.ONE);
                cfc.setForceOuterJoin(Boolean.valueOf(String.valueOf(j.get("outer_join"))) ? 1 : 0);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            return cfc;
        }).collect(Collectors.toList());
        String name = String.valueOf(jsonObject.get("name"));
        CompositeFeedDefinition ccc = new FeedService().createCompositeFeed(compositeFeedNodeList, compositeFeedConnectionList, name, conn);

        jo.put("url_key", ccc.getApiKey());
        return new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
