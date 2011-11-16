package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 10/6/11
 * Time: 4:26 PM
 */
public class MarmotHerderComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (pipelineData.getReport().getReportRunMarmotScript() != null) {
            Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
            Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : pipelineData.getAllItems()) {
                List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    keyMap.put(analysisItem.getKey().toKeyString(), items);
                }
                items.add(analysisItem);
            }
            for (AnalysisItem analysisItem : pipelineData.getAllItems()) {
                List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    displayMap.put(analysisItem.toDisplay(), items);
                }
                items.add(analysisItem);
            }
            StringTokenizer toker = new StringTokenizer(pipelineData.getReport().getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                for (IRow row : dataSet.getRows()) {
                    try {
                        new ReportCalculation(line).applyAfterReport(pipelineData.getReport(), pipelineData.getAllItems(), keyMap, displayMap, row);
                    } catch (ReportException re) {
                        throw re;
                    } catch (Exception e) {
                        throw new ReportException(new AnalysisItemFault(e.getMessage() + " in the calculation of report code " + line + ".", null));
                    }
                }
            }
        }/*

        for (AnalysisItem item : pipelineData.getReportItems()) {
            if (item.getMarmotScript() != null && !"".equals(item.getMarmotScript().trim())) {
                StringTokenizer toker = new StringTokenizer(item.getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    new FieldDecorationCalculationLogic(item, dataSet).calculate(line, pipelineData.getReport(),
                            pipelineData.getAllItems());
                }
            }
        }*/
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
