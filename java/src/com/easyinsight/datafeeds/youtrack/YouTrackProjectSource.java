package com.easyinsight.datafeeds.youtrack;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
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
    public static final String PROJECT_URL = "Project URL";

    public YouTrackProjectSource() {
        setFeedName("Projects");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(PROJECT_NAME, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_LEAD, new AnalysisDimension());
        fieldBuilder.addField(PROJECT_URL, new AnalysisDimension());
    }


    @Override
    public FeedType getFeedType() {
        return FeedType.YOUTRACK_PROJECTS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            HttpClient client = new HttpClient();
            Document doc = runRestRequest("/rest/admin/project", client, new Builder(), (YouTrackCompositeSource) parentDefinition);
            Nodes projects = doc.query("/projectRefs/project");
            DataSet dataSet = new DataSet();
            for (int i = 0; i < projects.size(); i++) {
                Element project = (Element) projects.get(i);
                String id = project.getAttribute("id").getValue();
                String url = project.getAttribute("url").getValue();
                Document detail = runRestRequest("/rest/admin/project/" + id, client, new Builder(), (YouTrackCompositeSource) parentDefinition);
                Element detailElement = (Element) detail.query("/project").get(0);
                String lead = "";
                if (detailElement.getAttribute("lead") != null) {
                    lead = detailElement.getAttribute("lead").getValue();
                }
                String name = "";
                if (detailElement.getAttribute("name") != null) {
                    name = detailElement.getAttribute("name").getValue();
                }
                IRow row = dataSet.createRow();
                row.addValue(keys.get(PROJECT_ID), id);
                row.addValue(keys.get(PROJECT_NAME), name);
                row.addValue(keys.get(PROJECT_LEAD), lead);
                row.addValue(keys.get(PROJECT_URL), url);
            }

            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
