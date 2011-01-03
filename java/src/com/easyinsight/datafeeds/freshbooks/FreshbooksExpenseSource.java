package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
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
    protected List<String> getKeys() {
        return Arrays.asList(EXPENSE_ID, CLIENT_ID, STAFF_ID, CATEGORY_ID, AMOUNT, DATE, NOTES,
                VENDOR, STATUS, COUNT, PROJECT_ID);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_EXPENSES;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
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

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksExpenseFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
