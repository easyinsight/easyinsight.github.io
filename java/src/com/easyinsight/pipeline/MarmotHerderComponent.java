package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.CalculationMetadata;
import com.easyinsight.calculations.NamespaceGenerator;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 10/6/11
 * Time: 4:26 PM
 */
public class MarmotHerderComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (pipelineData.getReport().getReportRunMarmotScript() != null && !"".equals(pipelineData.getReport().getReportRunMarmotScript())) {
            Map<String, UniqueKey> namespaceMap = null;

            List<AnalysisItem> allItems = new ArrayList<>(pipelineData.getAllItems());
            if (pipelineData.getReport() != null && pipelineData.getReport().getAddedItems() != null) {
                if (pipelineData.getConn() != null) {
                    namespaceMap = new NamespaceGenerator().generate(pipelineData.getReport().getDataFeedID(), pipelineData.getReport().getAddonReports(), pipelineData.getConn());
                }
                allItems.addAll(pipelineData.getReport().getAddedItems());
            }
            if (namespaceMap == null) {
                namespaceMap = new HashMap<>();
            }
            KeyDisplayMapper mapper = KeyDisplayMapper.create(allItems, pipelineData.getInsightRequestMetadata().isAvoidKeyDisplayCollisions());
            Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
            Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
            Map<String, List<AnalysisItem>> unqualifiedDisplayMap = mapper.getUnqualifiedDisplayMap();
            StringTokenizer toker = new StringTokenizer(pipelineData.getReport().getReportRunMarmotScript(), "\r\n");
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setReport(pipelineData.getReport());
            calculationMetadata.setDataSourceFields(allItems);
            calculationMetadata.setDataSet(dataSet);
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                for (IRow row : dataSet.getRows()) {
                    try {
                        new ReportCalculation(line).applyAfterReport(pipelineData.getReport(), allItems, keyMap, displayMap, unqualifiedDisplayMap, row, namespaceMap,
                                calculationMetadata);
                    } catch (ReportException re) {
                        throw re;
                    } catch (Exception e) {
                        throw new ReportException(new AnalysisItemFault(e.getMessage() + " in the calculation of report code " + line + ".", null));
                    }
                }
            }
        }

        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
