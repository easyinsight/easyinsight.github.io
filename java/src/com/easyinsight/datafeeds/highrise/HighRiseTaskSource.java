package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 11:50:45 PM
 */
public class HighRiseTaskSource extends HighRiseBaseSource {

    // body, category, created-at, done-at, due-at, id, owner-id, author-id, subject-id, subject-type, updated-at, frame, subject-name

    public static final String BODY = "Task Body";
    public static final String CATEGORY = "Task Category";
    public static final String DUE_AT = "Task Due At";
    public static final String DONE_AT = "Task Done At";
    public static final String CREATED_AT = "Task Created At";
    public static final String OWNER = "Task Owner";
    public static final String AUTHOR = "Task Author";
    public static final String CASE_ID = "Task Case ID";
    public static final String COMPANY_ID = "Task Company ID";
    public static final String CONTACT_ID = "Task Contact ID";
    public static final String DEAL_ID = "Task Deal ID";
    public static final String COUNT = "Task Count";
    public static final String TASK_ID = "Task ID";

    public HighRiseTaskSource() {
        setFeedName("Tasks");
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(BODY, CATEGORY, DUE_AT, DONE_AT, CREATED_AT, COUNT, OWNER, AUTHOR, CASE_ID, COMPANY_ID, TASK_ID, DEAL_ID, CONTACT_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(BODY), true));
        analysisItems.add(new AnalysisDimension(keys.get(TASK_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(CATEGORY), true));
        analysisItems.add(new AnalysisDimension(keys.get(OWNER), true));
        analysisItems.add(new AnalysisDimension(keys.get(AUTHOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(CASE_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(DEAL_ID), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(DUE_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(DONE_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_TASKS;
    }



    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;

        DataSet ds = new DataSet();
        try {
            Collection<TaskInfo> tasks = highRiseCompositeSource.getOrCreateCache(conn).getTaskInfos();
            for (TaskInfo task : tasks) {
                task.addToDataSet(ds);
            }
        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new HighRiseTask1To2(this));
    }
}
