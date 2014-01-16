package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportException;
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
public class InfusionsoftContactGroupCategorySource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String CATEGORY_NAME = "CategoryName";
    public static final String DESCRIPTION = "CategoryDescription";

    public InfusionsoftContactGroupCategorySource() {
        setFeedName("Tag Group");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_TAG_GROUP;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, CATEGORY_NAME, DESCRIPTION);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "Category ID"));
        analysisitems.add(new AnalysisDimension(keys.get(CATEGORY_NAME), "Category Name"));
        analysisitems.add(new AnalysisDimension(keys.get(DESCRIPTION), "Description"));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("ContactGroupCategory", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}