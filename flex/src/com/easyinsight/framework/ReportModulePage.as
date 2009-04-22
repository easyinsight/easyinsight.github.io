package com.easyinsight.framework {
import com.easyinsight.listing.IPerspective;
import com.easyinsight.solutions.InsightDescriptor;

public class ReportModulePage extends ModulePage{

    private var insightDescriptor:InsightDescriptor;

    public function ReportModulePage(insightDescriptor:InsightDescriptor) {
        super();
        this.insightDescriptor = insightDescriptor;
    }


    override protected function getModuleName():String {
        return super.getModuleName();
    }

    override protected function configure(perspective:IPerspective):void {
        super.configure(perspective);
    }
}
}