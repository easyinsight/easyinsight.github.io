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
            List<AnalysisItem> allItems = new ArrayList<AnalysisItem>(pipelineData.getAllItems());
            if (pipelineData.getReport() != null && pipelineData.getReport().getAddedItems() != null) {
                allItems.addAll(pipelineData.getReport().getAddedItems());
            }
            KeyDisplayMapper mapper = KeyDisplayMapper.create(allItems);
            Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
            Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
            StringTokenizer toker = new StringTokenizer(pipelineData.getReport().getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                for (IRow row : dataSet.getRows()) {
                    try {
                        new ReportCalculation(line).applyAfterReport(pipelineData.getReport(), allItems, keyMap, displayMap, row);
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
