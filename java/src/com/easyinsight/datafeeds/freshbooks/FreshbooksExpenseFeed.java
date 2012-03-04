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
 * Time: 3:26:08 PM
 */
public class FreshbooksExpenseFeed extends FreshbooksFeed {
    protected FreshbooksExpenseFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            FreshbooksCompositeSource parent = (FreshbooksCompositeSource) getParentSource(conn);
            if (!parent.isLiveDataSource()) {
                return super.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
            }
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
                String string = "<page>" + requestPage + "</page>";
                Document invoicesDoc = query("expense.list", string, conn);
                Nodes expenseNodes = invoicesDoc.query("/response/expenses");
                if (expenseNodes.size() > 0) {
                    Node invoicesSummaryNode = expenseNodes.get(0);
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
                        addValue(row, FreshbooksExpenseSource.STAFF_ID, staffID, keys);
                        addValue(row, FreshbooksExpenseSource.EXPENSE_ID, expenseID, keys);
                        addValue(row, FreshbooksExpenseSource.CLIENT_ID, clientID, keys);
                        addValue(row, FreshbooksExpenseSource.CATEGORY_ID, categoryID, keys);
                        addValue(row, FreshbooksExpenseSource.PROJECT_ID, projectID, keys);
                        addValue(row, FreshbooksExpenseSource.NOTES, notes, keys);
                        addValue(row, FreshbooksExpenseSource.VENDOR, vendor, keys);
                        if (amountString != null) {
                            addValue(row, FreshbooksExpenseSource.AMOUNT, Double.parseDouble(amountString), keys);
                        }
                        addValue(row, FreshbooksExpenseSource.DATE, invoiceDate, keys);
                        addValue(row, FreshbooksExpenseSource.COUNT, 1, keys);
                    }
                } else {
                    break;
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
