package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
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
 * Date: 3/21/11
 * Time: 7:36 PM
 */
public class HarvestProjectSource extends HarvestBaseSource {

    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static DateFormat DATE_FORMAT = new SimpleDateFormat(XMLDATEFORMAT);

    public static final String PROJECT_ID = "Project ID";
    public static final String PROJECT_NAME = "Project Name";
    public static final String PROJECT_ACTIVE = "Project Active";
    public static final String PROJECT_BILLABLE = "Billable Project";
    public static final String PROJECT_BILL_BY = "Project Bill-By";
    public static final String CLIENT_ID = "Project - Client ID";
    public static final String PROJECT_CODE = "Project Code";
    public static final String HOURLY = "Hourly Rate";
    public static final String NOTES = "Notes";
    public static final String BUDGET_BY = "Project Budget By";
    public static final String BUDGET = "Project Budget";
    public static final String FIRST_RECORD = "Project First Record At";
    public static final String LAST_RECORD = "Project Last Record At";
    public static final String PROJECT_COUNT = "Project Count";
    public static final String FEES = "Project Fees"; // FOR FUTURE USE



    public HarvestProjectSource() {
        setFeedName("Projects");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_PROJECT;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PROJECT_ID, PROJECT_NAME, PROJECT_ACTIVE, PROJECT_BILLABLE, PROJECT_BILL_BY, HOURLY,
                CLIENT_ID, PROJECT_CODE, NOTES, BUDGET_BY, BUDGET, FIRST_RECORD, LAST_RECORD, PROJECT_COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisDimension projectID = new AnalysisDimension(keys.get(PROJECT_ID), true);
        projectID.setHidden(true);
        analysisItems.add(projectID);
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_ACTIVE), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_BILLABLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_BILL_BY), true));
        analysisItems.add(new AnalysisMeasure(keys.get(HOURLY), HOURLY, AggregationTypes.AVERAGE, true, FormattingConfiguration.CURRENCY));
        AnalysisDimension clientID = new AnalysisDimension(keys.get(CLIENT_ID), true);
        clientID.setHidden(true);
        analysisItems.add(clientID);
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_CODE), true));
        analysisItems.add(new AnalysisText(keys.get(NOTES)));
        analysisItems.add(new AnalysisDimension(keys.get(BUDGET_BY), true));
        analysisItems.add(new AnalysisMeasure(keys.get(BUDGET), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDateDimension(keys.get(FIRST_RECORD), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(LAST_RECORD), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(PROJECT_COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet ds = new DataSet();
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document projects = source.getOrRetrieveProjects(client, builder);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String id = queryField(curProject, "id/text()");
                String name = queryField(curProject, "name/text()");
                String active = queryField(curProject, "active/text()");
                String billable = queryField(curProject, "billable/text()");
                String billBy = queryField(curProject, "bill-by/text()");
                String hourly = queryField(curProject, "hourly-rate/text()");
                String clientId = queryField(curProject, "client-id/text()");
                String code = queryField(curProject, "code/text()");
                String notes = queryField(curProject, "notes/text()");
                String budgetBy = queryField(curProject, "budget-by/text()");
                String budget = queryField(curProject, "budget/text()");
                String latestRecord = queryField(curProject, "hint-latest-record-at/text()");
                String earliestRecord = queryField(curProject, "hint-earliest-record-at/text()");
                IRow row = ds.createRow();
                row.addValue(keys.get(PROJECT_ID), id);
                row.addValue(keys.get(PROJECT_NAME), name);
                row.addValue(keys.get(PROJECT_ACTIVE), active);
                row.addValue(keys.get(PROJECT_BILLABLE), billable);
                row.addValue(keys.get(PROJECT_BILL_BY), billBy);
                row.addValue(keys.get(CLIENT_ID), clientId);
                row.addValue(keys.get(PROJECT_CODE), code);
                if(hourly != null && hourly.length() > 0)
                    row.addValue(keys.get(HOURLY), Double.parseDouble(hourly));
                row.addValue(keys.get(NOTES), notes);
                row.addValue(keys.get(BUDGET_BY), budgetBy);
                if(budget != null && budget.length() > 0)
                    row.addValue(keys.get(BUDGET), Double.parseDouble(budget));
                row.addValue(keys.get(LAST_RECORD), DATE_FORMAT.parse(latestRecord));
                row.addValue(keys.get(FIRST_RECORD), DATE_FORMAT.parse(earliestRecord));
                row.addValue(keys.get(PROJECT_COUNT), 1.0);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        }
        return ds;
    }
}
