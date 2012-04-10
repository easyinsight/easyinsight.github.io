package com.easyinsight.calculations;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.storage.IDataTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/3/12
 * Time: 12:23 PM
 */
public class DataSourceCalculationMetadata extends CalculationMetadata {
    private FeedDefinition dataSource;
    private List<IDataTransform> transforms = new ArrayList<IDataTransform>();

    public List<IDataTransform> getTransforms() {
        return transforms;
    }

    public void setTransforms(List<IDataTransform> transforms) {
        this.transforms = transforms;
    }

    public FeedDefinition getDataSource() {
        return dataSource;
    }

    public void setDataSource(FeedDefinition dataSource) {
        this.dataSource = dataSource;
    }
}
