package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;

import java.util.Map;
import java.util.HashMap;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 7:34:27 AM
 */
public class LinkDecorationComponent implements IComponent {


    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
            for (Link link : analysisItem.getLinks()) {
                if (link.generatesURL()) {
                    for (IRow row : dataSet.getRows()) {
                        String url = link.generateLink(row, pipelineData.getDataSourceProperties());
                        Value value = row.getValue(analysisItem.createAggregateKey());
                        Map<String, String> links = value.getLinks();
                        if (links == null) {
                            links = new HashMap<String, String>();
                            value.setLinks(links);
                        }
                        links.put(link.getLabel(), url);
                    }
                }
            }
        }

        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
