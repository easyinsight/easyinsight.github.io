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
 * Time: 3:26:08 PM
 */
public class FreshbooksExpenseFeed extends FreshbooksFeed {
    protected FreshbooksExpenseFeed(String url, String tokenKey, String tokenSecretKey) {
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
            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("expense.list", "");
                Node invoicesSummaryNode = invoicesDoc.query("/response/expenses").get(0);
                String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                pages = Integer.parseInt(pageString);
                currentPage = Integer.parseInt(currentPageString);
                Nodes invoices = invoicesDoc.query("/response/expenses/expense");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < invoices.size(); i++) {
                    Node invoice = invoices.get(i);
                    String expenseID = queryField(invoice, "expense_id/text()");
                    String staffID = queryField(invoice, "staff_id/text()");
                    String categoryID = queryField(invoice, "category_id/text()");
                    String projectID = queryField(invoice, "project_id/text()");
                    String clientID = queryField(invoice, "client_id/text()");
                    String notes = queryField(invoice, "notes/text()");
                    String vendor = queryField(invoice, "vendor/text()");
                    String amountString = queryField(invoice, "amount/text()");
                    String invoiceDateString = queryField(invoice, "date/text()");
                    Date invoiceDate = df.parse(invoiceDateString);
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(FreshbooksExpenseSource.STAFF_ID), staffID);
                    row.addValue(keys.get(FreshbooksExpenseSource.EXPENSE_ID), expenseID);
                    row.addValue(keys.get(FreshbooksExpenseSource.CLIENT_ID), clientID);
                    row.addValue(keys.get(FreshbooksExpenseSource.CATEGORY_ID), categoryID);
                    row.addValue(keys.get(FreshbooksExpenseSource.PROJECT_ID), projectID);
                    row.addValue(keys.get(FreshbooksExpenseSource.NOTES), notes);
                    row.addValue(keys.get(FreshbooksExpenseSource.VENDOR), vendor);
                    if (amountString != null)
                        row.addValue(keys.get(FreshbooksExpenseSource.AMOUNT), Double.parseDouble(amountString));
                    row.addValue(keys.get(FreshbooksExpenseSource.DATE), invoiceDate);
                    row.addValue(keys.get(FreshbooksExpenseSource.COUNT), 1);
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
