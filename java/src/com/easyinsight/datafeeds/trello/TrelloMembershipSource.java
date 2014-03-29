package com.easyinsight.datafeeds.trello;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/24/14
 * Time: 9:29 PM
 */
public class TrelloMembershipSource extends TrelloBaseSource {
    public static final String MEMBER_NAME = "Member Name";
    public static final String CARD_ID = "Membership Card ID";

    public TrelloMembershipSource() {
        setFeedName("Memberships");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.TRELLO_MEMBERSHIPS;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(MEMBER_NAME, new AnalysisDimension());
        fieldBuilder.addField(CARD_ID, new AnalysisDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            TrelloCompositeSource trelloCompositeSource = (TrelloCompositeSource) parentDefinition;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            Map<String, String> memberCache = new HashMap<String, String>();
            List<Member> c = trelloCompositeSource.getMemberData();
            for (Member d : c) {
                IRow row = dataSet.createRow();
                String memberID = d.getMemberID();
                String memberName = memberCache.get(memberID);
                if (memberName == null) {
                    JSONObject memberObj = runRequestForObject("https://api.trello.com/1/members/"  + memberID, httpClient, (TrelloCompositeSource) parentDefinition);
                    if (memberObj.get("fullName") != null) {
                        memberName = memberObj.get("fullName").toString();
                    } else if (memberObj.get("username") != null) {
                        memberName = memberObj.get("username").toString();
                    } else {
                        memberName = memberID;
                    }
                    memberCache.put(memberID, memberName);
                }
                row.addValue(keys.get(MEMBER_NAME), memberName);
                row.addValue(keys.get(CARD_ID), d.getCardID());
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
