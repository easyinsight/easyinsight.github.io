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

    public Pipeline setup(WSAnalysisDefinition report, Feed dataSource, InsightRequestMetadata insightRequestMetadata) {
        Set<AnalysisItem> allNeededAnalysisItems = compilePipelineData(report, dataSource, insightRequestMetadata);
        components = generatePipelineCommands(allNeededAnalysisItems, report.getAllAnalysisItems(), report.getFilterDefinitions(), report);
        return this;
    }

    protected abstract List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report);
         
    private Set<AnalysisItem> compilePipelineData(WSAnalysisDefinition report, Feed dataSource, InsightRequestMetadata insightRequestMetadata) {
        List<AnalysisItem> allRequestedAnalysisItems = new ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();
        if (report.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                allNeededAnalysisItems.add(filterDefinition.getField());
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
        pipelineData = new PipelineData(report, allNeededAnalysisItems, insightRequestMetadata, dataSource.getFields());
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

    public ListDataResults toList(DataSet dataSet) {
        for (IComponent component : components) {
            dataSet = component.apply(dataSet, pipelineData);
        }
        ListDataResults results = dataSet.toListDataResults(new ArrayList<AnalysisItem>(pipelineData.getReport().getAllAnalysisItems()));
        for (IComponent component : components) {
            component.decorate(results);
        }
        return results;
    }
}
