package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 6/28/12
 * Time: 9:26 AM
 */
public class HighRiseActivitySource extends HighRiseBaseSource {
    public static final String BODY = "Activity Body";
    public static final String CASE_ID = "Activity Case ID";
    public static final String ACTIVITY_ID = "Activity ID";
    public static final String ACTIVITY_TYPE = "Activity Type";
    public static final String CONTACT_ID = "Activity Contact ID";
    public static final String COMPANY_ID = "Activity Company ID";
    public static final String DEAL_ID = "Activity Deal ID";
    public static final String TASK_ID = "Activity Task ID";
    public static final String NOTE_CREATED_AT = "Activity Created At";
    public static final String NOTE_UPDATED_AT = "Activity Updated At";
    public static final String NOTE_AUTHOR = "Activity Author";
    public static final String COUNT = "Activity Count";

    public HighRiseActivitySource() {
        setFeedName("Activities");
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(BODY, CASE_ID, ACTIVITY_ID, CONTACT_ID, COMPANY_ID, DEAL_ID, TASK_ID, NOTE_CREATED_AT, NOTE_UPDATED_AT, NOTE_AUTHOR, COUNT, ACTIVITY_TYPE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisText(keys.get(BODY)));
        analysisItems.add(new AnalysisDimension(keys.get(CASE_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(ACTIVITY_TYPE), true));
        analysisItems.add(new AnalysisDimension(keys.get(ACTIVITY_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(DEAL_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TASK_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(NOTE_AUTHOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(NOTE_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(NOTE_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_ACTIVITIES;
    }

    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;

        DataSet ds = new DataSet();
        if (!highRiseCompositeSource.isIncludeCaseNotes()) {
            return ds;
        }
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");

        try {
            Date date;
            if (lastRefreshDate == null) {
                date = new Date(0);
            } else {
                date = lastRefreshDate;
            }
            HighriseRecordingsCache highriseActivitysCache = highRiseCompositeSource.getOrCreateRecordingsCache(client, date);

            List<Activity> activities = highriseActivitysCache.getActivities();

            Key noteKey = parentDefinition.getField(ACTIVITY_ID).toBaseKey();

            if (lastRefreshDate == null) {
                for (Activity Activity : activities) {
                    IRow row = ds.createRow();
                    activityToRow(Activity, row);
                }
            } else {
                for (Activity Activity : activities) {
                    ds = new DataSet();
                    IRow row = ds.createRow();
                    activityToRow(Activity, row);
                    StringWhere userWhere = new StringWhere(noteKey, "Note"+Activity.getId());
                    IDataStorage.updateData(ds, Arrays.asList((IWhere) userWhere));
                    ds = null;
                }
            }

            Collection<TaskInfo> tasks = highRiseCompositeSource.getOrCreateCache(conn).getTaskInfos();
            if (lastRefreshDate == null) {
                for (TaskInfo task : tasks) {
                    IRow row = ds.createRow();
                    taskToRow(task, row);
                }
            } else {
                for (TaskInfo task : tasks) {
                    ds = new DataSet();
                    IRow row = ds.createRow();
                    taskToRow(task, row);
                    StringWhere userWhere = new StringWhere(noteKey, "Task"+task.getTaskID());
                    IDataStorage.updateData(ds, Arrays.asList((IWhere) userWhere));
                    ds = null;
                }
            }

        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    @Override
    protected String getUpdateKeyName() {
        return ACTIVITY_ID;
    }

    private void taskToRow(TaskInfo taskInfo, IRow row) {
        row.addValue(BODY, taskInfo.getBody());
        row.addValue(ACTIVITY_ID, "Task"+taskInfo.getTaskID());
        row.addValue(ACTIVITY_TYPE, "Task");
        row.addValue(NOTE_AUTHOR, taskInfo.getAuthor());
        row.addValue(CONTACT_ID, taskInfo.getContactID());
        row.addValue(COMPANY_ID, taskInfo.getCompanyID());
        row.addValue(DEAL_ID, taskInfo.getDealID());
        row.addValue(CASE_ID, taskInfo.getCaseID());
        row.addValue(NOTE_CREATED_AT, new DateValue(taskInfo.getCreatedAt()));
        row.addValue(NOTE_UPDATED_AT, new DateValue(taskInfo.getCreatedAt()));
        row.addValue(COUNT, 1);
    }

    private void activityToRow(Activity activity, IRow row) {
        row.addValue(BODY, activity.getBody());
        row.addValue(ACTIVITY_ID, "Note"+activity.getId());
        row.addValue(ACTIVITY_TYPE, activity.getActivityType());
        row.addValue(NOTE_AUTHOR, activity.getAuthor());
        row.addValue(CONTACT_ID, activity.getContactID());
        row.addValue(COMPANY_ID, activity.getCompanyID());
        row.addValue(DEAL_ID, activity.getDealID());
        row.addValue(CASE_ID, activity.getCaseID());
        row.addValue(NOTE_CREATED_AT, new DateValue(activity.getCreatedAt()));
        row.addValue(NOTE_UPDATED_AT, new DateValue(activity.getUpdatedAt() != null ? activity.getUpdatedAt() : activity.getCreatedAt()));
        row.addValue(COUNT, 1);
    }
}
