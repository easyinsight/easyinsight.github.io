package com.easyinsight.rowutil;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 9:42 AM
 */
public class APIUtil {

    /**
     * Creates a <code>DataSourceFactory</code> used for defining a data source inside of Easy Insight. Use this method
     * as the starting point to any new code.
     * @param dataSourceName the name of the data source you wish to create inside of Easy Insight
     * @param apiKey your API key, as found in the Developer page in Easy Insight
     * @param apiSecretKey your API secret key, as found in the Developer page in Easy Insight
     * @return a <code>DataSourceFactory> object you can use to actually define metadata about the data source
     */
    public static DataSourceFactory defineDataSource(String dataSourceName, String apiKey, String apiSecretKey) {
        return new DataSourceFactory(apiKey, apiSecretKey, dataSourceName);
    }

    /**
     * Creates a <code>CompositeDataSourceFactory</code> used for defining a composite data source inside of Easy Insight
     * @param dataSourceName the name of the composite data source you wish to create inside of Easy Insight
     * @param apiKey your API key, as found in the Developer page in Easy Insight
     * @param apiSecretKey your API secret key, as found in the Developer page in Easy Insight
     * @return a <code>CompositeDataSourceFactory> object you can use to actually define metadata about the data source
     */
    public static CompositeDataSourceFactory defineCompositeDataSource(String dataSourceName, String apiKey, String apiSecretKey) {
        return new CompositeDataSourceFactory(dataSourceName, apiKey, apiSecretKey);
    }
}
