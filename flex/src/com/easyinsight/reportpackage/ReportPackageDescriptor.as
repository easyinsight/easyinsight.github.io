package com.easyinsight.reportpackage {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.reportpackage.ReportPackageDescriptor")]
public class ReportPackageDescriptor extends EIDescriptor {
    public function ReportPackageDescriptor() {
        super();
    }


    override public function getType():int {
        return EIDescriptor.PACKAGE;
    }
}
}