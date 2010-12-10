package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.dataset.DataSet;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 29, 2010
 * Time: 3:26:44 PM
 */
public class FreshbooksTaskFeed extends FreshbooksFeed {
    protected FreshbooksTaskFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey, parentSource);
    }


    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) throws ReportException {
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
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws ReportException {
        try {
            Map<String, Key> keys = new HashMap<String, Key>();
            for (AnalysisItem analysisItem : analysisItems) {
                keys.put(analysisItem.getKey().toKeyString(), analysisItem.createAggregateKey());
            }
            DataSet dataSet = new DataSet();

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("task.list", "<page>" + requestPage + "</page>");
                Node invoicesSummaryNode = invoicesDoc.query("/response/tasks").get(0);
                String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                pages = Integer.parseInt(pageString);
                currentPage = Integer.parseInt(currentPageString);
                Nodes invoices = invoicesDoc.query("/response/tasks/task");

                for (int i = 0; i < invoices.size(); i++) {
                    Node invoice = invoices.get(i);
                    String taskID = queryField(invoice, "task_id/text()");
                    String name = queryField(invoice, "name/text()");
                    String description = queryField(invoice, "description/text()");
                    String billable = queryField(invoice, "billable/text()");
                    String rate = queryField(invoice, "rate/text()");

                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(FreshbooksTaskSource.TASK_ID), taskID);
                    row.addValue(keys.get(FreshbooksTaskSource.NAME), name);
                    row.addValue(keys.get(FreshbooksTaskSource.DESCRIPTION), description);
                    row.addValue(keys.get(FreshbooksTaskSource.BILLABLE), billable);
                    if (rate != null) row.addValue(keys.get(FreshbooksTaskSource.RATE), Double.parseDouble(rate));
                    row.addValue(keys.get(FreshbooksTaskSource.COUNT), 1);
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
