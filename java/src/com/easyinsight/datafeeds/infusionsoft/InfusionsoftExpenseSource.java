package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/30/13
 * Time: 6:01 PM
 */
public class InfusionsoftExpenseSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String CONTACT_ID = "ContactId";
    public static final String EXPENSE_TYPE = "ExpenseType";
    public static final String EXPENSE_AMT = "ExpenseAmt";
    public static final String TYPE_ID = "TypeId";
    public static final String DATE_INCURRED = "DateIncurred";


    public InfusionsoftExpenseSource() {
        setFeedName("Expenses");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_EXPENSES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, CONTACT_ID, EXPENSE_TYPE, EXPENSE_AMT, TYPE_ID, DATE_INCURRED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "ID"));
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(EXPENSE_TYPE), "Expense Type"));
        analysisitems.add(new AnalysisDimension(keys.get(TYPE_ID), "Type ID"));
        analysisitems.add(new AnalysisMeasure(keys.get(EXPENSE_AMT), "Expense Amount", AggregationTypes.SUM));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_INCURRED), "Date Incurred", AnalysisDateDimension.DAY_LEVEL, true));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Expense", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}