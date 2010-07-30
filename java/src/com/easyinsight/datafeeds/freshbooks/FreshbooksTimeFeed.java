package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
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
    protected FreshbooksTimeFeed(String url, String tokenKey, String tokenSecretKey) {
        super(url, tokenKey, tokenSecretKey);
    }


    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        Set<AnalysisItem> set = new HashSet<AnalysisItem>();
        set.add(analysisItem);
        DataSet dataSet = getAggregateDataSet(set, new ArrayList<FilterDefinition>(), insightRequestMetadata, null, false);
        for (IRow row : dataSet.getRows()) {
            metadata.addValue(analysisItem, row.getValue(analysisItem.createAggregateKey()), insightRequestMetadata);
        }
        return metadata;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws TokenMissingException {
        try {
            Map<String, Key> keys = new HashMap<String, Key>();
            for (AnalysisItem analysisItem : analysisItems) {
                keys.put(analysisItem.getKey().toKeyString(), analysisItem.createAggregateKey());
            }
            DataSet dataSet = new DataSet();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("time_entry.list", "<page>" + requestPage + "</page>");
                Node invoicesSummaryNode = invoicesDoc.query("/response/time_entries").get(0);
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
                    row.addValue(keys.get(FreshbooksTimeEntrySource.TIME_ENTRY_ID), timeEntryID);
                    row.addValue(keys.get(FreshbooksTimeEntrySource.STAFF_ID), staffID);
                    row.addValue(keys.get(FreshbooksTimeEntrySource.PROJECT_ID), projectID);
                    row.addValue(keys.get(FreshbooksTimeEntrySource.TASK_ID), taskID);
                    row.addValue(keys.get(FreshbooksTimeEntrySource.NOTES), notes);
                    if (hoursString != null) row.addValue(keys.get(FreshbooksTimeEntrySource.HOURS), Double.parseDouble(hoursString));
                    row.addValue(keys.get(FreshbooksTimeEntrySource.DATE), entryDate);
                    row.addValue(keys.get(FreshbooksTimeEntrySource.COUNT), 1);
                }
                requestPage++;
            } while (currentPage < pages);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
