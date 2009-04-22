package com.easyinsight.framework {
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.listing.DataFeedDescriptor;
import com.easyinsight.listing.IPerspective;

import mx.managers.BrowserManager;

public class DescriptorModulePage extends ModulePage{

    private var dataFeedDescriptor:DataFeedDescriptor;

    public function DescriptorModulePage(dataFeedDescriptor:DataFeedDescriptor) {
        super();
        this.dataFeedDescriptor = dataFeedDescriptor;
    }

    override protected function getModuleName():String {
        return super.getModuleName();
    }

    override protected function configure(perspective:IPerspective):void {
        var dataAnalysisContainer:DataAnalysisContainer = perspective as DataAnalysisContainer;
        var dataService:DataService = new DataService();
        dataService.dataFeedID = dataFeedDescriptor.dataFeedID;
        dataAnalysisContainer.dataService = dataService;
        BrowserManager.getInstance().setTitle("Easy Insight - " + dataFeedDescriptor.name);
    }
}
}