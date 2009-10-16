package com.easyinsight.report {
import com.easyinsight.framework.ModuleAnalyzeSource;

import com.easyinsight.solutions.Solution;

import flash.display.DisplayObject;

import mx.collections.ArrayCollection;

public class MultiScreenAnalyzeSource extends ModuleAnalyzeSource{

    private var descriptors:ArrayCollection;
    private var solution:Solution;

    public function MultiScreenAnalyzeSource(descriptors:ArrayCollection, solution:Solution = null) {
        super();
        this.descriptors = descriptors;
        this.solution = solution;
    }

    override public function createDirect():DisplayObject {
        var view:MultiScreenView = new MultiScreenView();
        view.reports = descriptors;
        view.solution = solution;
        return view;
    }
}
}