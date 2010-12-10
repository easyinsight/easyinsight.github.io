package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.dataset.DataSet;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 29, 2010
 * Time: 3:26:20 PM
 */
public class FreshbooksCategoryFeed extends FreshbooksFeed {
    protected FreshbooksCategoryFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey, parentSource);
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) throws ReportException {
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
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws ReportException {
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
                Document invoicesDoc = query("category.list", "<page>" + requestPage + "</page>");
                Node invoicesSummaryNode = invoicesDoc.query("/response/categories").get(0);
                String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                pages = Integer.parseInt(pageString);
                currentPage = Integer.parseInt(currentPageString);
                Nodes invoices = invoicesDoc.query("/response/categories/category");
                for (int i = 0; i < invoices.size(); i++) {
                    Node invoice = invoices.get(i);
                    String categoryID = queryField(invoice, "category_id/text()");
                    String categoryName = queryField(invoice, "name/text()");
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(FreshbooksCategorySource.CATEGORY_ID), categoryID);
                    row.addValue(keys.get(FreshbooksCategorySource.CATEGORY_NAME), categoryName);
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
