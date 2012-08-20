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
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            FreshbooksCompositeSource parent = (FreshbooksCompositeSource) getParentSource(conn);
            if (!parent.isLiveDataSource()) {
                return super.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
            }
            Map<String, Collection<Key>> keys = new HashMap<String, Collection<Key>>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (analysisItem.isDerived()) {
                    continue;
                }
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
                Document invoicesDoc = query("estimate.list", "<page>" + requestPage + "</page>", conn);
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

                        String amountString = queryField(invoice, "amount/text()");
                        IRow row = dataSet.createRow();
                        addValue(row, FreshbooksEstimateSource.ESTIMATE_ID, invoiceID, keys);
                        addValue(row, FreshbooksEstimateSource.NUMBER, invoiceNumber, keys);
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
}
