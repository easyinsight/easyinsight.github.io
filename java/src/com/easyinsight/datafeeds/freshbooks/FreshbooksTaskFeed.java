package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
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

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("task.list", "<page>" + requestPage + "</page>", conn);
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
                    addValue(row, FreshbooksTaskSource.TASK_ID, taskID, keys);
                    addValue(row, FreshbooksTaskSource.NAME, name, keys);
                    addValue(row, FreshbooksTaskSource.DESCRIPTION, description, keys);
                    addValue(row, FreshbooksTaskSource.BILLABLE, billable, keys);
                    if (rate != null) {
                        addValue(row, FreshbooksTaskSource.RATE,  Double.parseDouble(rate), keys);
                    }
                    addValue(row, FreshbooksTaskSource.COUNT, 1, keys);
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
