package com.easyinsight.report {
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.reportpackage.ReportPackageDescriptor;

public class PackageAnalyzeSource extends PerspectiveInfo {

    public var packageDescriptor:ReportPackageDescriptor;
    public var installOption:Boolean = false;
    public var score:Number;
    public var originalPackageID:int;

    public function PackageAnalyzeSource(packageDescriptor:ReportPackageDescriptor, installOption:Boolean = false, originalPackageID:int = 0,
            score:Number = 0) {
        super(PerspectiveInfo.PACKAGE);
        var properties:Object = new Object();
        properties.packageID = packageDescriptor.id;
        properties.showAddBar = installOption;
        properties.originPackageID = originalPackageID;
        properties.originPackageScore = score;
        properties.properties = properties;
    }
}
}