package com.easyinsight.solutions;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.exchange.ExchangeItem;

import java.util.List;

/**
 * User: jamesboe
 * Date: May 20, 2010
 * Time: 1:55:46 PM
 */
public class ReportTemplateInfo {
    private List<DataSourceDescriptor> dataSources;
    private ExchangeItem exchangeData;

    public ReportTemplateInfo() {
    }

    public ReportTemplateInfo(List<DataSourceDescriptor> dataSources, ExchangeItem exchangeData) {
        this.dataSources = dataSources;
        this.exchangeData = exchangeData;
    }

    public ExchangeItem getExchangeData() {
        return exchangeData;
    }

    public void setExchangeData(ExchangeItem exchangeData) {
        this.exchangeData = exchangeData;
    }

    public List<DataSourceDescriptor> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceDescriptor> dataSources) {
        this.dataSources = dataSources;
    }
}
