package com.easyinsight.datafeeds.kashoo;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import nu.xom.ParsingException;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:43 PM
 */
public class KashooAccountSource extends KashooBaseSource {

    public static final String ACCOUNT_ID = "Account ID";
    public static final String ACCOUNT_NAME = "Account Name";
    public static final String ACCOUNT_NUMBER = "Account Number";
    public static final String BUSINESS_ID = "Business ID";

    public KashooAccountSource() {
        setFeedName("Accounts");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ACCOUNT_ID, ACCOUNT_NAME, ACCOUNT_NUMBER, BUSINESS_ID);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.KASHOO_ACCOUNTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(ACCOUNT_ID)));
        analysisItems.add(new AnalysisDimension(keys.get(ACCOUNT_NAME)));
        analysisItems.add(new AnalysisDimension(keys.get(ACCOUNT_NUMBER)));
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_ID)));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Token tokenObj = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.KASHOO, parentDefinition.getDataFeedID(), false, conn);
        HttpClient httpClient = getHttpClient();
        DataSet dataSet = new DataSet();
        try {

            JSONArray resp = (JSONArray) runRestRequest("/api/users/me/businesses", httpClient, parentDefinition, tokenObj.getTokenValue());
            for(Object o : resp) {
                JSONObject curObject = (JSONObject) o;

                String businessID = String.valueOf(curObject.get("id"));
                int page = 0;
                JSONArray responseObject;
                do {
                    responseObject = (JSONArray) runRestRequest("/api/businesses/" + businessID + "/accounts?limit=" + PAGE_LIMIT + "&offset=" + (page * PAGE_LIMIT), httpClient, parentDefinition, tokenObj.getTokenValue());
                    for(Object oo : responseObject) {
                        JSONObject jj = (JSONObject) oo;
                        IRow row = dataSet.createRow();
                        row.addValue(keys.get(ACCOUNT_ID), String.valueOf(jj.get("id")));
                        row.addValue(keys.get(ACCOUNT_NAME), String.valueOf(jj.get("name")));
                        row.addValue(keys.get(ACCOUNT_NUMBER), String.valueOf(jj.get("number")));
                        row.addValue(keys.get(BUSINESS_ID), String.valueOf(businessID));
                    }
                    page = page + 1;
                } while(responseObject.size() > 0);
            }
        } catch (ParsingException e) {
            throw new RuntimeException(e);
        }

        return dataSet;
    }
}
