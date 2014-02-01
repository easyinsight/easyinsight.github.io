package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;

import java.util.*;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 7:34:27 AM
 */
public class DrillThroughDecorationComponent implements IComponent {



    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        try {
            List<DrillThrough> passThroughDrillthroughs = new ArrayList<DrillThrough>();
            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                for (Link link : analysisItem.getLinks()) {
                    if (link instanceof DrillThrough) {
                        DrillThrough drillThrough = (DrillThrough) link;
                        if (drillThrough.getPassThroughField() != null) {
                            passThroughDrillthroughs.add(drillThrough);
                        }
                    }
                }
            }
            if (passThroughDrillthroughs.size() > 0) {
                for (DrillThrough drillThrough : passThroughDrillthroughs) {
                    AnalysisItem item = drillThrough.getPassThroughField().reconcileToAnalysisItem(pipelineData.getReport().getDataFeedID());
                    for (IRow row : dataSet.getRows()) {
                        for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                                Value value = row.getValue(analysisItem.createAggregateKey());
                                if (value.getDrillThroughs() != null && value.getDrillThroughs().containsKey(item.qualifiedName())) {
                                    List<Value> fieldValues = value.getDrillThroughs().get(item.qualifiedName());
                                    if (fieldValues != null) {
                                        value.setDrillThroughValues(new HashSet<Value>(fieldValues));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                for (Link link : analysisItem.getLinks()) {
                    if (link instanceof DrillThrough) {
                        DrillThrough drillThrough = (DrillThrough) link;
                        if (drillThrough.getPassThroughField() != null) {
                            AnalysisItem item = drillThrough.getPassThroughField().reconcileToAnalysisItem(pipelineData.getReport().getDataFeedID());
                            for (IRow row : dataSet.getRows()) {
                                Value value = row.getValue(analysisItem.createAggregateKey());
                                if (value.getDrillThroughValues() == null) {
                                    Map<String, Set<Value>> values = row.getPassthroughRow();
                                    if (values != null) {
                                        Set<Value> fieldValues = values.get(item.qualifiedName());
                                        if (fieldValues != null) {
                                            value.setDrillThroughValues(fieldValues);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
        /*try {
            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                for (Link link : analysisItem.getLinks()) {
                    if (link instanceof DrillThrough) {
                        DrillThrough drillThrough = (DrillThrough) link;
                        if (drillThrough.getPassThroughField() != null) {
                            AnalysisItem item = drillThrough.getPassThroughField().reconcileToAnalysisItem(pipelineData.getReport().getDataFeedID());
                            for (IRow row : dataSet.getRows()) {
                                Value value = row.getValue(analysisItem.createAggregateKey());
                                if (value.getDrillThroughs() != null && value.getDrillThroughs().containsKey(item.qualifiedName())) {
                                    List<Value> fieldValues = value.getDrillThroughs().get(item.qualifiedName());
                                    if (fieldValues != null) {
                                        value.getDrillThroughs().put(item.qualifiedName(), new ArrayList<Value>(new HashSet<Value>(fieldValues)));
                                    }
                                } else {
                                    Map<String, Set<Value>> values = row.getPassthroughRow();
                                    if (values != null) {
                                        Set<Value> fieldValues = values.get(item.qualifiedName());
                                        if (fieldValues != null) {
                                            Map<String, List<Value>> links = value.getDrillThroughs();
                                            if (links == null) {
                                                links = new HashMap<String, List<Value>>();
                                                value.setDrillThroughs(links);
                                            }
                                            links.put(drillThrough.getPassThroughField().getName(), new ArrayList<Value>(fieldValues));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
        }*/

        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
