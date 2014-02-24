package com.easyinsight.datafeeds.redbooth;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 7:22 PM
 */
public class RedboothOrganizationSource extends RedboothBaseSource {
    public static final String ID = "ID";
    public static final String NAME = "Name";

    public RedboothOrganizationSource() {
        setFeedName("Organizations");
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.REDBOOTH_ORGANIZATION;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        RedboothCompositeSource redboothCompositeSource = (RedboothCompositeSource) parentDefinition;
        DataSet dataSet = new DataSet();
        HttpClient httpClient = getHttpClient(redboothCompositeSource);
        Map base = (Map) queryList("/api/1/organizations", redboothCompositeSource, httpClient);
        List<Map> organizations = (List<Map>) base.get("objects");
        for (Map org : organizations) {
            IRow row = dataSet.createRow();
            row.addValue(keys.get(ID), getJSONValue(org, "id"));
            row.addValue(keys.get(NAME), getJSONValue(org, "name"));
        }
        return dataSet;
    }
}
