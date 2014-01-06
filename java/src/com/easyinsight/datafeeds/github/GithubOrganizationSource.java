package com.easyinsight.datafeeds.github;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/5/14
 * Time: 11:20 AM
 */
public class GithubOrganizationSource extends GithubBaseSource {

    public static final String ID = "ID";

    public GithubOrganizationSource() {
        setFeedName("Repository");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(ID), true));
        return items;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HttpClient client = new HttpClient();
        List blah = queryList("user/orgs", (GithubCompositeSource) parentDefinition, client);
        for (Object obj : blah) {
            Map map = (Map) obj;
            String id = map.get("id").toString();
            IRow row = dataSet.createRow();
            row.addValue(keys.get(ID), id);
        }
        return dataSet;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.GITHUB_REPOSITORY;
    }
}
