package com.easyinsight.datafeeds.custom;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.custom.client.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Credentials;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 8, 2009
 * Time: 8:52:22 PM
 */
public class CustomDataFeed extends Feed {

    private String wsdl;
    private DataProvider port;
    private static final QName SERVICE_NAME = new QName("http://sampleimpl.easyinsight.com/", "DataProviderService");

    public DataProvider getPort(Credentials credentials) {
        try {
            if (port == null) {
                URL url = new URL(wsdl);
                DataProviderService ss = new DataProviderService(url, SERVICE_NAME);
                BindingProvider provider = (BindingProvider) ss;
                Map<String, Object> requestContext = provider.getRequestContext();
                requestContext.put(BindingProvider.USERNAME_PROPERTY, credentials.getUserName());
                requestContext.put(BindingProvider.PASSWORD_PROPERTY, credentials.getPassword());
                port = ss.getDataProviderPort();
            }
            return port;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) throws TokenMissingException {
        List<String> keys = new ArrayList<String>();
        for (AnalysisItem analysisItem : analysisItems) {
            keys.add(analysisItem.getKey().toKeyString());
        }
        List<com.easyinsight.datafeeds.custom.client.Row> rows = port.getRows(keys, new Where(), new ArrayList<Property>());
        DataSet dataSet = new DataSet();
        for (com.easyinsight.datafeeds.custom.client.Row row : rows) {
            IRow localRow = dataSet.createRow();
            if (row.getStringValues() != null) {
                for (StringValue stringValue : row.getStringValues()) {
                    localRow.addValue(new NamedKey(stringValue.getKey()), stringValue.getValue());
                }
            }
            if (row.getNumberValues() != null) {
                for (NumberValue numberValue : row.getNumberValues()) {
                    localRow.addValue(new NamedKey(numberValue.getKey()), numberValue.getValue());
                }
            }
            if (row.getDateValues() != null) {
                for (DateValue dateValue : row.getDateValues()) {
                    localRow.addValue(new NamedKey(dateValue.getKey()), dateValue.getValue().toGregorianCalendar().getTime());
                }
            }
        }
        return dataSet;
    }

    @Override
    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
