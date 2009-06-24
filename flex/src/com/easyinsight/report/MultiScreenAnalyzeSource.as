package com.easyinsight.report {
import com.easyinsight.framework.ModuleAnalyzeSource;

import flash.display.DisplayObject;

import mx.collections.ArrayCollection;

public class MultiScreenAnalyzeSource extends ModuleAnalyzeSource{

    private var descriptors:ArrayCollection;

    public function MultiScreenAnalyzeSource(descriptors:ArrayCollection) {
        super();
        this.descriptors = descriptors;
    }

    override public function createDirect():DisplayObject {
        var view:MultiScreenView = new MultiScreenView();
        view.reports = descriptors;
        return view;
    }
}
}