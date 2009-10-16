package com.easyinsight.report {
import com.easyinsight.FullScreenPage;

import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.solutions.Solution;


import mx.collections.ArrayCollection;

public class MultiScreenAnalyzeSource implements AnalyzeSource{

    private var descriptors:ArrayCollection;
    private var solution:Solution;

    public function MultiScreenAnalyzeSource(descriptors:ArrayCollection, solution:Solution = null) {
        super();
        this.descriptors = descriptors;
        this.solution = solution;
    }

    public function createAnalysisPopup():FullScreenPage {
        var view:MultiScreenView = new MultiScreenView();
        view.reports = descriptors;
        view.solution = solution;
        return view;
    }
}
}