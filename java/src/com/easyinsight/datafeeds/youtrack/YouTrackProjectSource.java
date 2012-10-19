package com.easyinsight.datafeeds.youtrack;

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
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/16/12
 * Time: 5:51 PM
 */
public class YouTrackProjectSource extends YouTrackBaseSource {

    public static final String PROJECT_NAME = "Project Name";
    public static final String PROJECT_ID = "Project ID";
    public static final String PROJECT_LEAD = "Project Lead";

    public YouTrackProjectSource() {
        setFeedName("Projects");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PROJECT_NAME, PROJECT_LEAD, PROJECT_ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(PROJECT_NAME)));
        fields.add(new AnalysisDimension(keys.get(PROJECT_ID)));
        fields.add(new AnalysisDimension(keys.get(PROJECT_LEAD)));
        return fields;
    }


    @Override
    public FeedType getFeedType() {
        return FeedType.YOUTRACK_PROJECTS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Document doc = runRestRequest("/rest/admin/project", new HttpClient(), new Builder(), (YouTrackCompositeSource) parentDefinition);
            Nodes projects = doc.query("/projectRefs/project");
            DataSet dataSet = new DataSet();
            for (int i = 0; i < projects.size(); i++) {
                Element project = (Element) projects.get(i);
                String id = project.getAttribute("id").getValue();
                //String name = project.getAttribute("name").getValue();
                //String lead = project.getAttribute("lead").getValue();
                IRow row = dataSet.createRow();
                row.addValue(keys.get(PROJECT_ID), id);
                //row.addValue(keys.get(PROJECT_NAME), name);
               // row.addValue(keys.get(PROJECT_LEAD), lead);
            }

            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
