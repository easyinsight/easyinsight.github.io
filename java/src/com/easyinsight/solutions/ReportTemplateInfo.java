package com.easyinsight.solutions;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.exchange.SolutionReportExchangeItem;

import java.util.List;

/**
 * User: jamesboe
 * Date: May 20, 2010
 * Time: 1:55:46 PM
 */
public class ReportTemplateInfo {
    private List<DataSourceDescriptor> dataSources;
    private SolutionReportExchangeItem exchangeData;

    public ReportTemplateInfo() {
    }

    public ReportTemplateInfo(List<DataSourceDescriptor> dataSources, SolutionReportExchangeItem exchangeData) {
        this.dataSources = dataSources;
        this.exchangeData = exchangeData;
    }

    public SolutionReportExchangeItem getExchangeData() {
        return exchangeData;
    }

    public void setExchangeData(SolutionReportExchangeItem exchangeData) {
        this.exchangeData = exchangeData;
    }

    public List<DataSourceDescriptor> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceDescriptor> dataSources) {
        this.dataSources = dataSources;
    }
}
