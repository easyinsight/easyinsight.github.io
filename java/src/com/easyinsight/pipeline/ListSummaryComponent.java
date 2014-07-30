package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.calculations.CalculationMetadata;
import com.easyinsight.calculations.NamespaceGenerator;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: Oct 9, 2009
 * Time: 11:20:42 AM
 */
public class ListSummaryComponent implements IComponent {

    private Map<AnalysisMeasure, Value> aggregationMap = new HashMap<AnalysisMeasure, Value>();

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {

        Set<AnalysisItem> reportItems = new HashSet<AnalysisItem>(pipelineData.getReportItems());
        AnalysisItemRetrievalStructure structure = new AnalysisItemRetrievalStructure(null);
        structure.setReport(pipelineData.getReport());
        structure.setConn(pipelineData.getConn());
        List<IComponent> components = new CalcGraph().doFunGraphStuff(reportItems, pipelineData.getAllItems(), reportItems, null, structure,
                pipelineData.getInsightRequestMetadata());

        Iterator<IComponent> iter = components.iterator();
        while (iter.hasNext()) {
            IComponent component = iter.next();
            if (!(component instanceof CalculationComponent)) {
                iter.remove();
                continue;
            }
            CalculationComponent calculationComponent = (CalculationComponent) component;
            if (!calculationComponent.getAnalysisCalculation().isRecalculateSummary()) {
                iter.remove();
            }
        }

        DataSet tempSet = new DataSet();
        IRow tempRow = tempSet.createRow();
        for (AnalysisItem reportItem : pipelineData.getReportItems()) {
            if (reportItem.hasType(AnalysisItemTypes.MEASURE)) {
                if (reportItem.hasType(AnalysisItemTypes.CALCULATION)) {
                    AnalysisCalculation analysisCalculation = (AnalysisCalculation) reportItem;
                    if (analysisCalculation.isRecalculateSummary()) {
                        continue;
                    }
                }
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) reportItem;
                AggregationFactory aggregationFactory = new AggregationFactory(analysisMeasure, false);
                Aggregation aggregation = aggregationFactory.getAggregation();
                for (IRow row : dataSet.getRows()) {
                    Value value = row.getValue(analysisMeasure.createAggregateKey());
                    aggregation.addValue(value);
                }
                Value aggregateValue = aggregation.getValue();
                aggregationMap.put(analysisMeasure, aggregateValue);
                tempRow.addValue(analysisMeasure.createAggregateKey(), aggregateValue);
            }
        }
        for (IComponent component : components) {
            component.apply(tempSet, pipelineData);
        }
        if (pipelineData.getReport().getReportRunMarmotScript() != null && !"".equals(pipelineData.getReport().getReportRunMarmotScript())) {
            Map<String, UniqueKey> namespaceMap = null;

            List<AnalysisItem> allItems = new ArrayList<AnalysisItem>(pipelineData.getAllItems());
            if (pipelineData.getReport() != null && pipelineData.getReport().getAddedItems() != null) {
                if (pipelineData.getConn() != null) {
                    namespaceMap = new NamespaceGenerator().generate(pipelineData.getReport().getDataFeedID(), pipelineData.getReport().getAddonReports(), pipelineData.getConn());
                }
                allItems.addAll(pipelineData.getReport().getAddedItems());
            }
            if (namespaceMap == null) {
                namespaceMap = new HashMap<String, UniqueKey>();
            }
            KeyDisplayMapper mapper = KeyDisplayMapper.create(allItems);
            Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
            Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
            Map<String, List<AnalysisItem>> unqualifiedDisplayMap = mapper.getUnqualifiedDisplayMap();
            StringTokenizer toker = new StringTokenizer(pipelineData.getReport().getReportRunMarmotScript(), "\r\n");
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setReport(pipelineData.getReport());
            calculationMetadata.setDataSourceFields(allItems);
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();

                try {
                    new ReportCalculation(line).applyAfterReport(pipelineData.getReport(), allItems, keyMap, displayMap, unqualifiedDisplayMap, tempSet.getRow(0), namespaceMap,
                            calculationMetadata);
                } catch (ReportException re) {
                    throw re;
                } catch (Exception e) {
                    throw new ReportException(new AnalysisItemFault(e.getMessage() + " in the calculation of report code " + line + ".", null));
                }

            }
        }
        for (AnalysisItem reportItem : pipelineData.getReportItems()) {
            if (reportItem.hasType(AnalysisItemTypes.CALCULATION)) {
                aggregationMap.put((AnalysisMeasure) reportItem, tempSet.getRow(0).getValue(reportItem));
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
        for (Map.Entry<AnalysisMeasure, Value> entry : aggregationMap.entrySet()) {
           ReportFieldExtension extension = entry.getKey().getReportFieldExtension();
            if (extension != null && extension instanceof TextReportFieldExtension) {
                TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) extension;
                if (textReportFieldExtension.isIgnoreOnSummary()) {
                    continue;
                }
            }
            listDataResults.getAdditionalProperties().put("summary" + entry.getKey().qualifiedName(), entry.getValue());
        }
    }
}
