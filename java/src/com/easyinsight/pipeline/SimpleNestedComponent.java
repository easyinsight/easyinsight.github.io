package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/5/12
 * Time: 12:47 PM
 */
public class SimpleNestedComponent implements INestedComponent {
    private List<IComponent> components = new ArrayList<IComponent>();

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IComponent component : components) {
            dataSet = component.apply(dataSet, pipelineData);
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
