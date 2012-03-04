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
 * Date: Jul 28, 2010
 * Time: 6:49:58 PM
 */
public class FreshbooksTaskSource extends FreshbooksBaseSource {

    public static final String TASK_ID = "Task ID";
    public static final String NAME = "Task Name";
    public static final String DESCRIPTION = "Task Description";
    public static final String BILLABLE = "Task Billable";
    public static final String RATE = "Task Rate";
    public static final String COUNT = "Task Count";

    public FreshbooksTaskSource() {
        setFeedName("Tasks");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(TASK_ID, NAME, DESCRIPTION, BILLABLE, RATE, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_TASKS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksTaskSource.TASK_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTaskSource.NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTaskSource.DESCRIPTION), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksTaskSource.BILLABLE), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksTaskSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksTaskSource.RATE), RATE, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
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
                Document invoicesDoc = query("task.list", "<page>" + requestPage + "</page>", freshbooksCompositeSource);
                Nodes nodes = invoicesDoc.query("/response/tasks");
                if (nodes.size() > 0) {
                    Node invoicesSummaryNode = nodes.get(0);
                    String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                    String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                    pages = Integer.parseInt(pageString);
                    currentPage = Integer.parseInt(currentPageString);
                    Nodes invoices = invoicesDoc.query("/response/tasks/task");

                    for (int i = 0; i < invoices.size(); i++) {
                        Node invoice = invoices.get(i);
                        String taskID = queryField(invoice, "task_id/text()");
                        String name = queryField(invoice, "name/text()");
                        String description = queryField(invoice, "description/text()");
                        String billable = queryField(invoice, "billable/text()");
                        String rate = queryField(invoice, "rate/text()");

                        IRow row = dataSet.createRow();
                        addValue(row, FreshbooksTaskSource.TASK_ID, taskID, keys);
                        addValue(row, FreshbooksTaskSource.NAME, name, keys);
                        addValue(row, FreshbooksTaskSource.DESCRIPTION, description, keys);
                        addValue(row, FreshbooksTaskSource.BILLABLE, billable, keys);
                        if (rate != null) {
                            addValue(row, FreshbooksTaskSource.RATE,  Double.parseDouble(rate), keys);
                        }
                        addValue(row, FreshbooksTaskSource.COUNT, 1, keys);
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
        return new FreshbooksTaskFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
