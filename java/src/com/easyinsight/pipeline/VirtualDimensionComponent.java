package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:08:23 PM
 */
public class VirtualDimensionComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        List<IRow> rows = dataSet.getRows();
        Collection<VirtualDimension> virtualDimensions = identifyVirtualDimensions(pipelineData.getReportItems());
        for (VirtualDimension virtualDimension : virtualDimensions) {
            rows = virtualDimension.createDimensions(rows, pipelineData.getReportItems());
        }
        return new DataSet(rows);
    }

    public void decorate(DataResults listDataResults) {
    }

    private Collection<VirtualDimension> identifyVirtualDimensions(Collection<AnalysisItem> analysisItems) {
        Map<Long, VirtualDimension> map = new HashMap<Long, VirtualDimension>();
        /*for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.getVirtualDimension() != null) {
                VirtualDimension virtualDimension = analysisItem.getVirtualDimension();
                map.put(virtualDimension.getVirtualDimensionID(), virtualDimension);
            }
        }*/
        return map.values();
    }

}
