package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.analysis.*;
import com.easyinsight.pipeline.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 8/23/11
 * Time: 4:09 PM
 */
public class ACSPipeline extends Pipeline {
    @Override
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems) {
        List<IComponent> components = new ArrayList<IComponent>();

        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                date.setDateLevel(AnalysisDateDimension.MONTH_LEVEL);
            }
        }
        components.add(new TypeTransformComponent(false));

        components.add(new NormalizationComponent());
        components.add(new AggregationComponent());
        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if ("Calc Wtd Procedures".equals(analysisItem.toDisplay())) {
                components.add(new CalculationComponent((AnalysisCalculation) analysisItem));
            }
        }
        return components;
    }
}
