package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.CalcGraph;
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
        List<IComponent> components = new CalcGraph().doFunGraphStuff(reportItems, pipelineData.getAllItems(), reportItems, true, new AnalysisItemRetrievalStructure());
        components.addAll(new CalcGraph().doFunGraphStuff(reportItems, pipelineData.getAllItems(), reportItems, false, new AnalysisItemRetrievalStructure()));

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
        for (AnalysisItem reportItem : pipelineData.getReportItems()) {
            if (reportItem.hasType(AnalysisItemTypes.CALCULATION)) {
                aggregationMap.put((AnalysisMeasure) reportItem, tempSet.getRow(0).getValue(reportItem));
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
        for (Map.Entry<AnalysisMeasure, Value> entry : aggregationMap.entrySet()) {
            listDataResults.getAdditionalProperties().put("summary" + entry.getKey().qualifiedName(), entry.getValue());
        }
    }
}
