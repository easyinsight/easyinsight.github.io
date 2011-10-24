package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportCalculation;
import com.easyinsight.calculations.FieldDecorationCalculationLogic;
import com.easyinsight.dataset.DataSet;

import java.util.StringTokenizer;

/**
 * User: jamesboe
 * Date: 10/6/11
 * Time: 4:26 PM
 */
public class MarmotHerderComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (pipelineData.getReport().getReportRunMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(pipelineData.getReport().getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                for (IRow row : dataSet.getRows()) {
                    new ReportCalculation(line).applyAfterReport(pipelineData.getReport(), pipelineData.getAllItems(), row);
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
