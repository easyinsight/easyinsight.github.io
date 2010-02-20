package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:19:19 PM
 */
public abstract class Pipeline {

    private List<IComponent> components = new ArrayList<IComponent>();
    private PipelineData pipelineData;
    private ResultsBridge resultsBridge = new ListResultsBridge();

    public Pipeline setup(WSAnalysisDefinition report, Feed dataSource, InsightRequestMetadata insightRequestMetadata) {
        Set<AnalysisItem> allNeededAnalysisItems = compilePipelineData(report, dataSource, insightRequestMetadata, report.getAddedItems());
        components = generatePipelineCommands(allNeededAnalysisItems, report.getAllAnalysisItems(), report.retrieveFilterDefinitions(), report);
        if (report.hasCustomResultsBridge()) {
            resultsBridge = report.getCustomResultsBridge();
        }
        return this;
    }

    protected abstract List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report);
         
    private Set<AnalysisItem> compilePipelineData(WSAnalysisDefinition report, Feed dataSource, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> additionalFields) {
        List<AnalysisItem> allRequestedAnalysisItems = new ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();
        if (report.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                allNeededAnalysisItems.addAll(filterDefinition.getField().getAnalysisItems(dataSource.getFields(), allRequestedAnalysisItems, false));
            }
        }
        for (AnalysisItem item : allRequestedAnalysisItems) {
            if (item.isValid()) {
                allNeededAnalysisItems.addAll(item.getAnalysisItems(dataSource.getFields(), allRequestedAnalysisItems, false));
                allNeededAnalysisItems.addAll(item.addLinkItems(dataSource.getFields(), allRequestedAnalysisItems));
                if (item.isVirtual()) {
                    allNeededAnalysisItems.add(item);
                }
            }
        }
        allNeededAnalysisItems.addAll(report.getLimitFields());
        /// TODO: this needs to factor in user added fields
        // there's the question of resolution here as well, really need a uniquely defined name for fields
        // have to bubble that up to visibility
        // that's what we're sort of defining as Key right now, but without the appropriate unique constraint

        List<AnalysisItem> masterFieldList = new ArrayList<AnalysisItem>(dataSource.getFields());
        if (additionalFields != null) {
            masterFieldList.addAll(additionalFields);
        }
        

        pipelineData = new PipelineData(report, allNeededAnalysisItems, insightRequestMetadata, dataSource.getFields(), dataSource.getProperties());
        return allNeededAnalysisItems;
    }

    protected final Collection<AnalysisItem> items(int type, Collection<AnalysisItem> items) {
        Collection<AnalysisItem> matchingItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : items) {
            if (item.hasType(type)) {
                matchingItems.add(item);
            }
        }
        return matchingItems;
    }

    public DataSet toDataSet(DataSet dataSet) {
        for (IComponent component : components) {
            dataSet = component.apply(dataSet, pipelineData);
        }
        return dataSet;
    }

    public DataResults toList(DataSet dataSet) {
        for (IComponent component : components) {
            dataSet = component.apply(dataSet, pipelineData);
        }
        DataResults results = resultsBridge.toDataResults(dataSet, new ArrayList<AnalysisItem>(pipelineData.getReport().getAllAnalysisItems()));
        for (IComponent component : components) {
            component.decorate(results);
        }
        return results;
    }
}
