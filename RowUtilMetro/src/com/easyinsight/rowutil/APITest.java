package com.easyinsight.rowutil;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:00 AM
 */
public class APITest {
    public static void main(String[] args) throws DatatypeConfigurationException {
        String apiKey = "xrnnCqtaFhZN";
        String apiSecretKey = "IzxHGLKiErchVLdo";
        DataSourceFactory ds1 = APIUtil.defineDataSource("Sample Data", apiKey, apiSecretKey);
        ds1.addGrouping("Blah");
        ds1.addMeasure("Measure1");
        DataSourceOperationFactory dataSourceOperationFactory = ds1.defineDataSource();
        DataSourceTarget target1 = dataSourceOperationFactory.replaceRowsOperation();
        DataRow row1 = target1.newRow();
        row1.addValue("Blah", "XYZ");
        row1.addValue("Measure1", 50);
        target1.flush();

        DataSourceFactory ds2 = APIUtil.defineDataSource("Sample Data 2", apiKey, apiSecretKey);
        ds2.addGrouping("Bah");
        DataSourceOperationFactory dataSourceOperationFactory2 = ds2.defineDataSource();
        DataSourceTarget target2 = dataSourceOperationFactory2.replaceRowsOperation();
        DataRow row2 = target2.newRow();
        row2.addValue("Bah", "XYZ");
        target2.flush();

        CompositeDataSourceFactory comb = APIUtil.defineCompositeDataSource("Combined Samples", apiKey, apiSecretKey);
        comb.addDataSource(dataSourceOperationFactory2.getDataSourceKey());
        comb.addDataSource(dataSourceOperationFactory.getDataSourceKey());
        comb.joinDataSources(dataSourceOperationFactory.getDataSourceKey(), "Blah", dataSourceOperationFactory2.getDataSourceKey(), "Bah");
        comb.defineCompositeSource();
    }
}
