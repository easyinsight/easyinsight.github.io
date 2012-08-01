package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/26/12
 * Time: 11:06 AM
 */
public class BasecampNextProjectSource extends BasecampNextBaseSource {

    public static final String PROJECT_ID = "Project ID";
    public static final String PROJECT_NAME = "Project Name";
    public static final String DESCRIPTION = "Project Description";
    public static final String UPDATED_AT = "Project Updated At";
    public static final String URL = "Project URL";

    public BasecampNextProjectSource() {
        setFeedName("Projects");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BASECAMP_NEXT_PROJECTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PROJECT_ID, PROJECT_NAME, UPDATED_AT, URL, DESCRIPTION);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(PROJECT_ID), PROJECT_ID));
        analysisitems.add(new AnalysisDimension(keys.get(PROJECT_NAME), PROJECT_NAME));
        analysisitems.add(new AnalysisDimension(keys.get(DESCRIPTION), DESCRIPTION));
        analysisitems.add(new AnalysisDimension(keys.get(URL), URL));
        analysisitems.add(new AnalysisDateDimension(keys.get(UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            BasecampNextCompositeSource basecampNextCompositeSource = (BasecampNextCompositeSource) parentDefinition;
            List<Project> projects = basecampNextCompositeSource.getOrCreateProjectCache().getProjects();
            for (Project project : projects) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(PROJECT_ID), project.getId());
                row.addValue(keys.get(PROJECT_NAME), project.getName());
                row.addValue(keys.get(DESCRIPTION), project.getDescription());
                row.addValue(keys.get(URL), project.getUrl());
                row.addValue(keys.get(UPDATED_AT), project.getUpdatedAt());
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
