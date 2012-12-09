package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.HierarchyLevel;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/5/12
 * Time: 12:47 PM
 */
public class HierarchyComponent implements INestedComponent {
    private List<IComponent> components = new ArrayList<IComponent>();

    private AnalysisHierarchyItem hierarchyItem;

    public HierarchyComponent(AnalysisHierarchyItem hierarchyItem) {
        this.hierarchyItem = hierarchyItem;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {

        List<DataSet> sets = new ArrayList<DataSet>();
        List<PipelineData> pipelineDatas = new ArrayList<PipelineData>();

        for (int i = 1; i < hierarchyItem.getHierarchyLevels().size(); i++) {
            try {
                sets.add(dataSet.clone());
                pipelineDatas.add(pipelineData.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        for (IComponent component : components) {
            dataSet = component.apply(dataSet, pipelineData);
        }

        for (int i = hierarchyItem.getHierarchyLevels().size() - 1; i > 0; i--) {
            int position = hierarchyItem.getHierarchyLevels().size() - i - 1;
            DataSet childSet = sets.get(position);
            PipelineData pipelineData1 = pipelineDatas.get(position);
            pipelineData1.getAllRequestedItems().remove(hierarchyItem.getHierarchyLevels().get(i).getAnalysisItem());
            pipelineData1.getReportItems().remove(hierarchyItem.getHierarchyLevels().get(i).getAnalysisItem());
            for (IComponent component : components) {
                childSet = component.apply(childSet, pipelineData1);
            }
            dataSet.addSet(childSet);
        }

        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }

    public void add(IComponent component) {
        components.add(component);
    }

    public void addAll(List<IComponent> components) {
        this.components.addAll(components);
    }
}
