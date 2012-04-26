package com.easyinsight.storage;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 4/3/12
 * Time: 11:36 AM
 */
public class MultiCacheDataTransform implements IDataTransform {


    private FeedDefinition baseSource;
    private Collection<MultiCacheInfo> infos;

    // cache("ACS2", "Calc Weighted Procedures", "Weighted Procedures")


    public MultiCacheDataTransform(Collection<MultiCacheInfo> infos, FeedDefinition baseSource) {
        this.infos = infos;
        this.baseSource = baseSource;
    }

    public void handle(EIConnection conn, IRow row) {

        //System.out.println("*** " + calculation.getCalculationString());

        DataSet dataSet = new DataSet();
        Object providerID = null;

        System.out.println("related provider = " + providerID);


        Feed feed = FeedRegistry.instance().getFeed(baseSource.getDataFeedID());

        WSListDefinition blah = new WSListDefinition();
        blah.setDataFeedID(baseSource.getDataFeedID());
        blah.setFilterDefinitions(new ArrayList<FilterDefinition>());
        List<AnalysisItem> columns = new ArrayList<AnalysisItem>();
        List<AnalysisCalculation> calculations = new ArrayList<AnalysisCalculation>();
        for (MultiCacheInfo multiCacheInfo : infos) {
            columns.add(multiCacheInfo.getCalculation());
            calculations.add(multiCacheInfo.getCalculation());
        }
        blah.setColumns(columns);
        CacheCalculationPipeline pipeline = new CacheCalculationPipeline(calculations);
        pipeline.setup(blah, feed, new InsightRequestMetadata());
        DataSet resultSet = pipeline.toDataSet(dataSet);
        IRow result = resultSet.getRow(0);
        for (MultiCacheInfo multiCacheInfo : infos) {
            Value targetValue = result.getValue(multiCacheInfo.getCalculation());
            System.out.println("result value = " + targetValue + " for " + multiCacheInfo.getEndField().getKey().toKeyString());
            row.addValue(multiCacheInfo.getEndField().getKey(), targetValue);
        }
    }
}
