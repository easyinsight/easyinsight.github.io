package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 4/2/11
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class HarvestExpenseSource extends HarvestBaseSource {
    public static final String EXPENSE_CATEGORY_ID = "Expenses - Expense Category";
    public static final String ID = "Expenses ID";
    public static final String NOTES = "Expenses Notes";
    public static final String PROJECT_ID = "Expenses Project ID";
    public static final String SPENT_AT = "Expenses Spent At";
    public static final String TOTAL_COST = "Expenses Total Cost";
    public static final String UNITS = "Expenses Units";
    public static final String USER_ID = "Expenses - User ID";
    public static final String IS_CLOSED = "Expenses Closed";

    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static DateFormat DATE_FORMAT = new SimpleDateFormat(XMLDATEFORMAT);
    public static final String QUERYDATEFORMAT = "yyyyMMdd";
    public static DateFormat OUT_DATE = new SimpleDateFormat(QUERYDATEFORMAT);
    public static final String UPDATED_SINCE_STRING= "yyyy-MM-dd HH:mm";
    public static final DateFormat UPDATED_SINCE_FORMAT = new SimpleDateFormat(UPDATED_SINCE_STRING);

    public HarvestExpenseSource() {
        setFeedName("Expenses");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_EXPENSES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(EXPENSE_CATEGORY_ID, ID, NOTES, PROJECT_ID, SPENT_AT, TOTAL_COST, UNITS, USER_ID, IS_CLOSED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem id = new AnalysisDimension(keys.get(ID), true);
        id.setHidden(true);
        analysisItems.add(id);
        AnalysisItem categoryId = new AnalysisDimension(keys.get(EXPENSE_CATEGORY_ID), true);
        categoryId.setHidden(true);
        analysisItems.add(categoryId);
        analysisItems.add(new AnalysisText(keys.get(NOTES)));
        AnalysisItem projectId = new AnalysisDimension(keys.get(PROJECT_ID), true);
        projectId.setHidden(true);
        analysisItems.add(projectId);
        analysisItems.add(new AnalysisDateDimension(keys.get(SPENT_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(TOTAL_COST), TOTAL_COST, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisMeasure(keys.get(UNITS), AggregationTypes.SUM));
        AnalysisItem userId = new AnalysisDimension(keys.get(USER_ID), true);
        userId.setHidden(true);
        analysisItems.add(userId);
        analysisItems.add(new AnalysisDimension(keys.get(IS_CLOSED), true));
        return analysisItems;
    }

    @Override
    protected String getUpdateKeyName() {
        return ID;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document projects = source.getOrRetrieveProjects(client, builder);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String projectId = queryField(curProject, "id/text()");
                String latestRecord = queryField(curProject, "hint-latest-record-at/text()");
                String earliestRecord = queryField(curProject, "hint-earliest-record-at/text()");
                String reqString = "/projects/" + projectId + "/expenses?from=" + OUT_DATE.format(DATE_FORMAT.parse(earliestRecord)) + "&to=" + OUT_DATE.format(DATE_FORMAT.parse(latestRecord));
                if(lastRefreshDate != null) {
                    reqString += "&updated_since=" + URLEncoder.encode(UPDATED_SINCE_FORMAT.format(lastRefreshDate), "UTF-8");
                }
                Document entries = runRestRequest(reqString, client, builder, source.getUrl(), true, source, false);
                Nodes entryNodes = entries.query("/expenses/expense");
                if(lastRefreshDate == null) {
                    DataSet ds = new DataSet();

                    for(int j = 0;j < entryNodes.size();j++) {
                        IRow curRow = ds.createRow();
                        populateRow(keys, entryNodes.get(j), curRow);
                    }
                    IDataStorage.insertData(ds);
                } else {
                    for(int j = 0;j < entryNodes.size();j++) {
                        DataSet ds = new DataSet();
                        IRow curRow = ds.createRow();
                        populateRow(keys, entryNodes.get(j), curRow);
                        IWhere stringWhere = new StringWhere(keys.get(ID), curRow.getValue(keys.get(ID)).toString());
                        IDataStorage.updateData(ds, Arrays.asList(stringWhere));
                    }
                }
            }
        } catch (ReportException re) {
            // ignore
            re.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void populateRow(Map<String, Key> keys, Node curNode, IRow curRow) throws ParseException {
        String id = queryField(curNode, "id/text()");
        String expenseCategory = queryField(curNode, "expense-category-id/text()");
        String notes = queryField(curNode, "notes/text()");
        String curProjectId = queryField(curNode, "project-id/text()");
        String spentAt = queryField(curNode, "spent-at/text()");
        String totalCost = queryField(curNode, "total-cost/text()");
        String units = queryField(curNode, "units/text()");
        String userId = queryField(curNode, "user-id/text()");
        String isClosed = queryField(curNode, "is-closed/text()");
        curRow.addValue(keys.get(ID), id);
        curRow.addValue(keys.get(EXPENSE_CATEGORY_ID), expenseCategory);
        curRow.addValue(keys.get(NOTES), notes);
        curRow.addValue(keys.get(PROJECT_ID), curProjectId);
        if(spentAt != null && spentAt.length() > 0)
            curRow.addValue(keys.get(SPENT_AT), DATE_FORMAT.parse(spentAt));
        if(totalCost != null && totalCost.length() > 0)
            curRow.addValue(keys.get(TOTAL_COST), Double.parseDouble(totalCost));
        if(units != null && units.length() > 0)
            curRow.addValue(keys.get(UNITS), Double.parseDouble(units));
        curRow.addValue(keys.get(USER_ID), userId);
        curRow.addValue(keys.get(IS_CLOSED), isClosed);
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }
}
