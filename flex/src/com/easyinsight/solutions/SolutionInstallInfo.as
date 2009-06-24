package com.easyinsight.solutions {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.SolutionInstallInfo")]
public class SolutionInstallInfo {

    public var previousID:int;
    public var descriptor:EIDescriptor;
    public var requiresConfiguration:Boolean;

    public function SolutionInstallInfo() {
    }
}
}