package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 29, 2010
 * Time: 3:26:51 PM
 */
public class FreshbooksTimeFeed extends FreshbooksFeed {
    protected FreshbooksTimeFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            Map<String, Collection<Key>> keys = new HashMap<String, Collection<Key>>();
            for (AnalysisItem analysisItem : analysisItems) {
                Collection<Key> keyColl = keys.get(analysisItem.getKey().toKeyString());
                if (keyColl == null) {
                    keyColl = new ArrayList<Key>();
                    keys.put(analysisItem.getKey().toKeyString(), keyColl);
                }
                keyColl.add(analysisItem.createAggregateKey());
            }
            DataSet dataSet = new DataSet();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("time_entry.list", "<page>" + requestPage + "</page>", conn);
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
}
