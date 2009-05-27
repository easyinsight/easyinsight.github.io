package com.easyinsight.solutions {
[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.SolutionInstallInfo")]
public class SolutionInstallInfo {

    public static const DATA_SOURCE:int = 1;
    public static const REPORT:int = 2;

    public var previousID:int;
    public var newID:int;
    public var type:int;

    public function SolutionInstallInfo() {
    }
}
}