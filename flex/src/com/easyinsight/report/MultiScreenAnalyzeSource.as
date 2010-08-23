package com.easyinsight.report {
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.solutions.Solution;


import mx.collections.ArrayCollection;

public class MultiScreenAnalyzeSource extends PerspectiveInfo {

    public function MultiScreenAnalyzeSource(descriptors:ArrayCollection, solution:Solution = null) {
        super(PerspectiveInfo.MULTI_SCREEN_VIEW, new Object());
        properties.reports = descriptors;
        properties.solution = solution;
    }
}
}