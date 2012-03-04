package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 28, 2010
 * Time: 6:50:08 PM
 */
public class FreshbooksTimeEntrySource extends FreshbooksBaseSource {

    public static final String TIME_ENTRY_ID = "Time Entry ID";
    public static final String STAFF_ID = "Staff ID";
    public static final String PROJECT_ID = "Project ID";
    public static final String TASK_ID = "Task ID";
    public static final String HOURS = "Time Entry Hours";
    public static final String DATE = "Time Entry Date";
    public static final String NOTES = "Time Entry Notes";
    public static final String COUNT = "Time Entry Count";

    public FreshbooksTimeEntrySource() {
        setFeedName("Time Entries");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(TIME_ENTRY_ID, STAFF_ID, PROJECT_ID, TASK_ID, HOURS, DATE, NOTES, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_TIME_ENTRIES;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys,
                                                  Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.TIME_ENTRY_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.STAFF_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.PROJECT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.TASK_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTimeEntrySource.NOTES), true));

        items.add(new AnalysisDateDimension(keys.get(FreshbooksTimeEntrySource.DATE), true, AnalysisDateDimension.DAY_LEVEL));

        items.add(new AnalysisMeasure(keys.get(FreshbooksTimeEntrySource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksTimeEntrySource.HOURS), AggregationTypes.SUM));        

        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parentDefinition;
        if (freshbooksCompositeSource.isLiveDataSource()) {
            return new DataSet();
        }
        try {
            DataSet dataSet = new DataSet();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("time_entry.list", "<page>" + requestPage + "</page>", freshbooksCompositeSource);
                Nodes timeNodes = invoicesDoc.query("/response/time_entries");
                if (timeNodes.size() == 0) {
                    return dataSet;
                }
                Node invoicesSummaryNode = timeNodes.get(0);
                String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                pages = Integer.parseInt(pageString);
                currentPage = Integer.parseInt(currentPageString);
                Nodes invoices = invoicesDoc.query("/response/time_entries/time_entry");

                for (int i = 0; i < invoices.size(); i++) {
                    Node invoice = invoices.get(i);
                    String timeEntryID = queryField(invoice, "time_entry_id/text()");
                    String staffID = queryField(invoice, "staff_id/text()");
                    String projectID = queryField(invoice, "project_id/text()");
                    String taskID = queryField(invoice, "task_id/text()");
                    String notes = queryField(invoice, "notes/text()");
                    String hoursString = queryField(invoice, "hours/text()");
                    String timeDateString = queryField(invoice, "date/text()");
                    Date entryDate = df.parse(timeDateString);
                    IRow row = dataSet.createRow();
                    addValue(row, FreshbooksTimeEntrySource.TIME_ENTRY_ID, timeEntryID, keys);
                    addValue(row, FreshbooksTimeEntrySource.STAFF_ID, staffID, keys);
                    addValue(row, FreshbooksTimeEntrySource.PROJECT_ID, projectID, keys);
                    addValue(row, FreshbooksTimeEntrySource.TASK_ID, taskID, keys);
                    addValue(row, FreshbooksTimeEntrySource.NOTES, notes, keys);
                    if (hoursString != null) {
                        addValue(row, FreshbooksTimeEntrySource.HOURS, Double.parseDouble(hoursString), keys);
                    }
                    addValue(row, FreshbooksTimeEntrySource.DATE, entryDate, keys);
                    addValue(row, FreshbooksTimeEntrySource.COUNT, 1, keys);
                }
                requestPage++;
            } while (currentPage < pages);
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksTimeFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
