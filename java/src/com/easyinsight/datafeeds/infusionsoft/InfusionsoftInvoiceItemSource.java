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
 * Time: 5:08 PM
 */
public class InfusionsoftInvoiceItemSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String INVOICE_ID = "InvoiceId";
    public static final String ORDER_ITEM_ID = "OrderItemId";
    public static final String INVOICE_AMT = "InvoiceAmt";
    public static final String DISCOUNT = "Discount";
    public static final String DATE_CREATED = "DateCreated";
    public static final String DESCRIPTION = "Description";
    public static final String COMMISSION_STATUS = "CommissionStatus";

    public InfusionsoftInvoiceItemSource() {
        setFeedName("Invoice Item");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_INVOICE_ITEM;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, INVOICE_ID, ORDER_ITEM_ID, INVOICE_AMT, DISCOUNT, DATE_CREATED, DESCRIPTION, COMMISSION_STATUS);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "ID"));
        analysisitems.add(new AnalysisDimension(keys.get(INVOICE_ID), "Invoice ID"));
        analysisitems.add(new AnalysisDimension(keys.get(ORDER_ITEM_ID), "Order Item ID"));
        analysisitems.add(new AnalysisMeasure(keys.get(INVOICE_AMT), "Invoice Amount", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(DISCOUNT), "Discount", AggregationTypes.SUM));
        analysisitems.add(new AnalysisDimension(keys.get(DESCRIPTION), "Description"));
        analysisitems.add(new AnalysisDimension(keys.get(COMMISSION_STATUS), "Commission Status"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), "Date Created", AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("InvoiceItem", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
