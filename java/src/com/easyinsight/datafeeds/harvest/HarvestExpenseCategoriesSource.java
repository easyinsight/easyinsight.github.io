package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 4/2/11
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class HarvestExpenseCategoriesSource extends HarvestBaseSource {

    public static final String ID = "Expense Categories ID";
    public static final String NAME = "Expense Categories Name";
    public static final String UNIT_NAME = "Expense Categories Unit Name";
    public static final String UNIT_PRICE = "Expense Categories Unit Price";
    public static final String DEACTIVATED = "Expense Categories Deactivated";

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, NAME, UNIT_NAME, UNIT_PRICE, DEACTIVATED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem idDimension = new AnalysisDimension(keys.get(ID), true);
        idDimension.setHidden(true);
        analysisItems.add(idDimension);
        analysisItems.add(new AnalysisDimension(keys.get(NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(UNIT_NAME), true));
        analysisItems.add(new AnalysisMeasure(keys.get(UNIT_PRICE), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(keys.get(DEACTIVATED), true));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_EXPENSE_CATEGORIES;
    }

    public HarvestExpenseCategoriesSource() {
        setFeedName("Expense Categories");
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document categories = runRestRequest("/expense_categories", client, builder, source.getUrl(), true, source, false);
            Nodes categoryNodes = categories.query("/expense-categories/expense-category");
            for(int i = 0;i < categoryNodes.size();i++) {
                Node curCategory = categoryNodes.get(i);
                String id = queryField(curCategory, "id/text()");
                String name = queryField(curCategory, "name/text()");
                String unitName = queryField(curCategory, "unit-name/text()");
                String unitPrice = queryField(curCategory,  "unit-price/text()");
                String deactivated = queryField(curCategory, "deactivated/text()");
                IRow row = dataSet.createRow();
                row.addValue(keys.get(ID), id);
                row.addValue(keys.get(NAME), name);
                row.addValue(keys.get(UNIT_NAME), unitName);
                if(unitPrice != null && unitPrice.length() > 0)
                    row.addValue(keys.get(UNIT_PRICE), Double.parseDouble(unitPrice));
                row.addValue(keys.get(DEACTIVATED), deactivated);
            }
        } catch (ParsingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
