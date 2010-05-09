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
        Set<AnalysisItem> allNeededAnalysisItems = compilePipelineData(report, dataSource, insightRequestMetadata);
        components = generatePipelineCommands(allNeededAnalysisItems, pipelineData.getAllRequestedItems(), report.retrieveFilterDefinitions(), report);
        if (report.hasCustomResultsBridge()) {
            resultsBridge = report.getCustomResultsBridge();
        }
        return this;
    }

    protected abstract List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report);
         
    private Set<AnalysisItem> compilePipelineData(WSAnalysisDefinition report, Feed dataSource, InsightRequestMetadata insightRequestMetadata) {

        List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(dataSource.getFields());
        if (report.getAddedItems() != null) {
            allFields.addAll(report.getAddedItems());
        }

        Set<AnalysisItem> allRequestedAnalysisItems = report.getAllAnalysisItems();
        allRequestedAnalysisItems.remove(null);

        Map<AnalysisItem, Integer> refMap = new HashMap<AnalysisItem, Integer>();

        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();
        if (report.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                List<AnalysisItem> items = filterDefinition.getAnalysisItems(allFields, allRequestedAnalysisItems, false, true);
                updateCount(items, refMap);
                allNeededAnalysisItems.addAll(items);
            }
        }
        for (AnalysisItem item : allRequestedAnalysisItems) {
            if (item.isValid()) {
                List<AnalysisItem> baseItems = item.getAnalysisItems(allFields, allRequestedAnalysisItems, false, true);
                updateCount(baseItems, refMap);
                allNeededAnalysisItems.addAll(baseItems);
                List<AnalysisItem> linkItems = item.addLinkItems(allFields, allRequestedAnalysisItems);
                updateCount(linkItems, refMap);
                allNeededAnalysisItems.addAll(linkItems);
                if (item.isVirtual()) {
                    updateCount(item, refMap);
                    allNeededAnalysisItems.add(item);
                }
            }
        }
        updateCount(report.getLimitFields(), refMap);
        allNeededAnalysisItems.addAll(report.getLimitFields());
        

        pipelineData = new PipelineData(report, allNeededAnalysisItems, insightRequestMetadata, allFields, dataSource.getProperties(), allRequestedAnalysisItems, refMap);
        return allNeededAnalysisItems;
    }

    private void updateCount(AnalysisItem analysisItem, Map<AnalysisItem, Integer> refMap) {
        Integer count = refMap.get(analysisItem);
        if (count == null) {
            refMap.put(analysisItem, 1);
        } else {
            refMap.put(analysisItem, count + 1);
        }
    }

    private void updateCount(Collection<AnalysisItem> analysisItems, Map<AnalysisItem, Integer> refMap) {
        for (AnalysisItem analysisItem : analysisItems) {
            Integer count = refMap.get(analysisItem);
            if (count == null) {
                refMap.put(analysisItem, 1);
            } else {
                refMap.put(analysisItem, count + 1);
            }
        }
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
        DataResults results = resultsBridge.toDataResults(dataSet, new ArrayList<AnalysisItem>(pipelineData.getAllRequestedItems()));
        for (IComponent component : components) {
            component.decorate(results);
        }
        return results;
    }
}
