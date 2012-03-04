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
 * Time: 6:48:41 PM
 */
public class FreshbooksExpenseSource extends FreshbooksBaseSource {
    public static final String EXPENSE_ID = "Expense ID";
    public static final String CLIENT_ID = "Expense Client ID";
    public static final String STAFF_ID = "Expense Staff ID";
    public static final String CATEGORY_ID = "Expense Category ID";
    public static final String PROJECT_ID = "Expense Project ID";
    public static final String AMOUNT = "Expense Amount";
    public static final String DATE = "Expense Date";
    public static final String NOTES = "Expense Notes";
    public static final String VENDOR = "Expense Vendor";
    public static final String STATUS = "Expense Status";
    public static final String COUNT = "Expense Count";

    public FreshbooksExpenseSource() {
        setFeedName("Expenses");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(EXPENSE_ID, CLIENT_ID, STAFF_ID, CATEGORY_ID, AMOUNT, DATE, NOTES,
                VENDOR, STATUS, COUNT, PROJECT_ID);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_EXPENSES;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksExpenseSource.EXPENSE_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksExpenseSource.CLIENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksExpenseSource.PROJECT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksExpenseSource.CATEGORY_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksExpenseSource.STAFF_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksExpenseSource.NOTES), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksExpenseSource.VENDOR), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksExpenseSource.STATUS), true));
        items.add(new AnalysisDateDimension(keys.get(FreshbooksExpenseSource.DATE), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisMeasure(keys.get(FreshbooksExpenseSource.AMOUNT), FreshbooksExpenseSource.AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisMeasure(keys.get(FreshbooksExpenseSource.COUNT), AggregationTypes.SUM));
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parentDefinition;
        if (freshbooksCompositeSource.isLiveDataSource()) {
            return new DataSet();
        }
        try {
            DataSet dataSet = new DataSet();
            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                String string = "<page>" + requestPage + "</page>";
                Document invoicesDoc = query("expense.list", string, freshbooksCompositeSource);
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

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksExpenseFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
