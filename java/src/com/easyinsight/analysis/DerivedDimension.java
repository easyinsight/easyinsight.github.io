package com.easyinsight.analysis;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Apr 20, 2009
 * Time: 10:58:02 AM
 */
public class DerivedDimension extends AnalysisDimension {
    private AnalysisDimension wrappedDimension;
    private VirtualDimension virtualDimension;

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        items.add(virtualDimension.getBaseDimension());
        return items;
    }
}
