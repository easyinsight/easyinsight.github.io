package com.easyinsight.datafeeds.kashoo;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:43 PM
 */
public class KashooBusinessSource extends KashooBaseSource {

    public static final String BUSINESS_ID = "Business ID";
    public static final String BUSINESS_NAME = "Business Name";

    public KashooBusinessSource() {
        setFeedName("Business");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(BUSINESS_ID, BUSINESS_NAME);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_ID)));
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_NAME)));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Token tokenObj = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.KASHOO, parentDefinition.getDataFeedID(), false, conn);
        // TODO: change to json encoding for auth key, per https://www.kashoo.com/api-docs/
        HttpClient httpClient = getHttpClient(tokenObj.getTokenValue(), "");
        DataSet dataSet = new DataSet();
        return dataSet;
    }
}
