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
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 29, 2010
 * Time: 2:19:00 PM
 */
public class FreshbooksCategorySource extends FreshbooksBaseSource {
    public static final String CATEGORY_ID = "Category ID";
    public static final String CATEGORY_NAME = "Category Name";

    public FreshbooksCategorySource() {
        setFeedName("Categories");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CATEGORY_ID, CATEGORY_NAME);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_CATEGORIES;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksCategorySource.CATEGORY_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksCategorySource.CATEGORY_NAME), true));
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
                Document invoicesDoc = query("category.list", "<page>" + requestPage + "</page>", freshbooksCompositeSource);
                Nodes nodes = invoicesDoc.query("/response/categories");
                if (nodes.size() > 0) {
                    Node invoicesSummaryNode = nodes.get(0);
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
                        addValue(row, FreshbooksCategorySource.CATEGORY_ID, categoryID, keys);
                        addValue(row, FreshbooksCategorySource.CATEGORY_NAME, categoryName, keys);
                    }
                    requestPage++;
                } else {
                    break;
                }
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
        return new FreshbooksCategoryFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
