package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.CalcGraph;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.JoinMetadata;
import com.easyinsight.etl.LookupTable;

import java.util.*;

/**
 * User: jamesboe
 * Date: 3/13/11
 * Time: 12:16 PM
 */
public class AltCompositeReportPipeline extends Pipeline {
    private Collection<JoinMetadata> joinMetadatas;

    public AltCompositeReportPipeline(Collection<JoinMetadata> joinItems) {
        this.joinMetadatas = joinItems;
    }

    @Override
    protected List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata) {
        List<IComponent> components = new ArrayList<IComponent>();
        for (AnalysisItem analysisItem : allNeededAnalysisItems) {
            if (analysisItem.getLookupTableID() != null && analysisItem.getLookupTableID() > 0) {
                LookupTable lookupTable = new FeedService().getLookupTable(analysisItem.getLookupTableID());
                if (lookupTable.getSourceField().hasType(AnalysisItemTypes.LISTING)) {
                    AnalysisList analysisList = (AnalysisList) lookupTable.getSourceField();
                    if (analysisList.isMultipleTransform()) components.add(new TagTransformComponent(analysisList));
                } else if (lookupTable.getSourceField().hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                    Set<AnalysisItem> analysisItems = new HashSet<AnalysisItem>();
                    analysisItems.add(lookupTable.getSourceField());
                    components.addAll(new CalcGraph().doFunGraphStuff(analysisItems, allItems, reportItems, Pipeline.BEFORE, new AnalysisItemRetrievalStructure(Pipeline.BEFORE)));
                }
                components.add(new LookupTableComponent(lookupTable));
            }
        }
        List<AnalysisItem> joinItems = new ArrayList<AnalysisItem>();
        for (JoinMetadata joinMetadata : joinMetadatas) {
            joinItems.add(joinMetadata.analysisItem);
        }
        components.addAll(new CalcGraph().doFunGraphStuff(new HashSet<AnalysisItem>(joinItems), allItems, reportItems, Pipeline.BEFORE, new AnalysisItemRetrievalStructure(Pipeline.BEFORE)));
        for (JoinMetadata joinMetadata : joinMetadatas) {
            AnalysisItem item = joinMetadata.analysisItem;
            for (AnalysisItem tag : items(AnalysisItemTypes.LISTING, joinItems)) {
                AnalysisList analysisList = (AnalysisList) tag;
                if (analysisList.isMultipleTransform()) components.add(new TagTransformComponent(analysisList));
            }
            if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                components.add(new DateTransformComponent(item));
            }
        }
        return components;
    }
}
