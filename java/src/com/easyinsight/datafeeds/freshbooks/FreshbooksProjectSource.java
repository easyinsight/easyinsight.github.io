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
 * Time: 2:58:17 PM
 */
public class FreshbooksProjectSource extends FreshbooksBaseSource {

    public static final String PROJECT_ID = "Project ID";
    public static final String NAME = "Project Name";
    public static final String DESCRIPTION = "Project Description";
    public static final String RATE = "Project Rate";
    public static final String BILL_METHOD = "Project Billing Method";
    public static final String CLIENT_ID = "Client ID";
    public static final String BUDGET_HOURS = "Project Budget";
    public static final String COUNT = "Project Count";

    public FreshbooksProjectSource() {
        setFeedName("Projects");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PROJECT_ID, CLIENT_ID, NAME, DESCRIPTION, RATE, BILL_METHOD, BUDGET_HOURS, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_PROJECTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.PROJECT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.CLIENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.DESCRIPTION), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksProjectSource.BILL_METHOD), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksProjectSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksProjectSource.BUDGET_HOURS), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksProjectSource.RATE), AggregationTypes.SUM));
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
                Document invoicesDoc = query("project.list", "<page>" + requestPage + "</page>", freshbooksCompositeSource);
                Nodes nodes = invoicesDoc.query("/response/projects");
                if (nodes.size() > 0) {
                    Node invoicesSummaryNode = nodes.get(0);
                    String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                    String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                    pages = Integer.parseInt(pageString);
                    currentPage = Integer.parseInt(currentPageString);
                    Nodes invoices = invoicesDoc.query("/response/projects/project");

                    for (int i = 0; i < invoices.size(); i++) {
                        Node invoice = invoices.get(i);
                        String projectID = queryField(invoice, "project_id/text()");
                        String name = queryField(invoice, "name/text()");
                        String description = queryField(invoice, "description/text()");
                        String billMethod = queryField(invoice, "bill_method/text()");
                        String clientID = queryField(invoice, "client_id/text()");
                        String rateString = queryField(invoice, "rate/text()");
                        String budgetString = queryField(invoice, "hour_budget/text()");
                        IRow row = dataSet.createRow();
                        addValue(row, FreshbooksProjectSource.PROJECT_ID, projectID, keys);
                        addValue(row, FreshbooksProjectSource.CLIENT_ID, clientID, keys);
                        addValue(row, FreshbooksProjectSource.DESCRIPTION, description, keys);
                        addValue(row, FreshbooksProjectSource.NAME, name, keys);
                        addValue(row, FreshbooksProjectSource.BILL_METHOD, billMethod, keys);
                        if (rateString != null) {
                            addValue(row, FreshbooksProjectSource.RATE, Double.parseDouble(rateString), keys);
                        }
                        if (budgetString != null) {
                            addValue(row, FreshbooksProjectSource.BUDGET_HOURS, Double.parseDouble(budgetString), keys);
                        }
                        addValue(row, FreshbooksProjectSource.COUNT, 1, keys);
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
        return new FreshbooksProjectFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new FreshbooksProject1To2(this));
    }
}
