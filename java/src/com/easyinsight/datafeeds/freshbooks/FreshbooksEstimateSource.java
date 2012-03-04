package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
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
 * Date: Jul 29, 2010
 * Time: 5:31:28 PM
 */
public class FreshbooksEstimateSource extends FreshbooksBaseSource {

    public static final String ESTIMATE_ID = "Estimate ID";
    public static final String NUMBER = "Estimate Number";
    public static final String CLIENT_ID = "Estimate Client ID";
    public static final String AMOUNT = "Estimate Amount";
    public static final String NOTES = "Estimate Notes";
    public static final String TERMS = "Estimate Terms";
    public static final String COUNT = "Estimate Count";
    public static final String DATE = "Estimate Date";
    public static final String STATUS = "Estimate Status";

    public FreshbooksEstimateSource() {
        setFeedName("Estimates");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ESTIMATE_ID, CLIENT_ID, AMOUNT, NUMBER, TERMS, NOTES, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_ESTIMATES;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.ESTIMATE_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.NUMBER), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.CLIENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.NOTES), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.TERMS), true));
        /*items.add(new AnalysisDimension(keys.get(FreshbooksEstimateSource.STATUS), true));
        items.add(new AnalysisDateDimension(keys.get(FreshbooksEstimateSource.DATE), true, AnalysisDateDimension.DAY_LEVEL));*/
        items.add(new AnalysisMeasure(keys.get(FreshbooksEstimateSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksEstimateSource.AMOUNT), AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));                
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parentDefinition;
        if (freshbooksCompositeSource.isLiveDataSource()) {
            return new DataSet();
        }
        try {
            DataSet dataSet = new DataSet();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("estimate.list", "<page>" + requestPage + "</page>", freshbooksCompositeSource);
                Nodes nodes = invoicesDoc.query("/response/estimates");
                if (nodes.size() > 0) {
                    Node invoicesSummaryNode = nodes.get(0);
                    String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                    String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                    pages = Integer.parseInt(pageString);
                    currentPage = Integer.parseInt(currentPageString);
                    Nodes invoices = invoicesDoc.query("/response/estimates/estimate");

                    for (int i = 0; i < invoices.size(); i++) {
                        Node invoice = invoices.get(i);
                        String invoiceID = queryField(invoice, "estimate_id/text()");
                        String invoiceNumber = queryField(invoice, "number/text()");
                        String clientID = queryField(invoice, "client_id/text()");
                        String notes = queryField(invoice, "notes/text()");
                        String terms = queryField(invoice, "terms/text()");
                        String status = queryField(invoice, "status/text()");
                        String dateString = queryField(invoice, "date/text()");
                        Date date = df.parse(dateString);

                        String amountString = queryField(invoice, "amount/text()");
                        IRow row = dataSet.createRow();
                        addValue(row, FreshbooksEstimateSource.ESTIMATE_ID, invoiceID, keys);
                        addValue(row, FreshbooksEstimateSource.NUMBER, invoiceNumber, keys);
                        /*addValue(row, FreshbooksEstimateSource.STATUS, status, keys);
                        addValue(row, FreshbooksEstimateSource.DATE, date, keys);*/
                        addValue(row, FreshbooksEstimateSource.NOTES, notes, keys);
                        addValue(row, FreshbooksEstimateSource.TERMS, terms, keys);
                        addValue(row, FreshbooksEstimateSource.CLIENT_ID, clientID, keys);
                        if (amountString != null) {
                            addValue(row, FreshbooksEstimateSource.AMOUNT, Double.parseDouble(amountString), keys);
                        }
                        addValue(row, FreshbooksEstimateSource.COUNT, 1, keys);
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
    public int getVersion() {
        return 1;
    }

    /*@Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new FreshbooksEstimate1To2(this));
    }*/

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksEstimateFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
