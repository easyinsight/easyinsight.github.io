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
 * Time: 6:01 PM
 */
public class InfusionsoftTagSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String TAG_NAME = "GroupName";
    public static final String TAG_CATEGORY_ID = "GroupCategoryId";
    public static final String TAG_DESCRIPTION = "GroupDescription";


    public InfusionsoftTagSource() {
        setFeedName("Tags");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_TAG;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, TAG_NAME, TAG_CATEGORY_ID, TAG_DESCRIPTION);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "ID"));
        analysisitems.add(new AnalysisDimension(keys.get(TAG_NAME), "Tag Name"));
        analysisitems.add(new AnalysisDimension(keys.get(TAG_CATEGORY_ID), "Tag Category ID"));
        analysisitems.add(new AnalysisDimension(keys.get(TAG_DESCRIPTION), "Tag Description"));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("ContactGroup", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}