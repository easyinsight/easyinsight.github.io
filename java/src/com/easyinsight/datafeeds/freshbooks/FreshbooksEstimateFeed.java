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
 * Time: 5:31:39 PM
 */
public class FreshbooksEstimateFeed extends FreshbooksFeed {
    protected FreshbooksEstimateFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey);
    }


    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        Set<AnalysisItem> set = new HashSet<AnalysisItem>();
        set.add(analysisItem);
        DataSet dataSet = getAggregateDataSet(set, new ArrayList<FilterDefinition>(), insightRequestMetadata, null, false, conn);
        for (IRow row : dataSet.getRows()) {
            metadata.addValue(analysisItem, row.getValue(analysisItem.createAggregateKey()), insightRequestMetadata);
        }
        return metadata;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
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
                Document invoicesDoc = query("estimate.list", "<page>" + requestPage + "</page>", conn);
                Node invoicesSummaryNode = invoicesDoc.query("/response/estimates").get(0);
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

                    String amountString = queryField(invoice, "amount/text()");
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(FreshbooksEstimateSource.ESTIMATE_ID), invoiceID);
                    row.addValue(keys.get(FreshbooksEstimateSource.NUMBER), invoiceNumber);
                    row.addValue(keys.get(FreshbooksEstimateSource.NOTES), notes);
                    row.addValue(keys.get(FreshbooksEstimateSource.TERMS), terms);
                    row.addValue(keys.get(FreshbooksEstimateSource.CLIENT_ID), clientID);
                    if (amountString != null) row.addValue(keys.get(FreshbooksEstimateSource.AMOUNT), Double.parseDouble(amountString));                    
                    row.addValue(keys.get(FreshbooksEstimateSource.COUNT), 1);
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
