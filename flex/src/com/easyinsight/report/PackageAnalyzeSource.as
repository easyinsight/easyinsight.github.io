package com.easyinsight.report {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.reportpackage.ReportPackageDescriptor;

public class PackageAnalyzeSource implements AnalyzeSource {

    public var packageDescriptor:ReportPackageDescriptor;
    public var installOption:Boolean = false;
    public var score:Number;
    public var originalPackageID:int;

    public function PackageAnalyzeSource(packageDescriptor:ReportPackageDescriptor, installOption:Boolean = false, originalPackageID:int = 0,
            score:Number = 0) {
        this.packageDescriptor = packageDescriptor;
        this.installOption = installOption;
        this.originalPackageID = originalPackageID;
        this.score = score;
    }

    public function createAnalysisPopup():FullScreenPage {
        var view:ReportPackageView = new ReportPackageView();
        view.packageID = packageDescriptor.id;
        view.showAddBar = installOption;
        view.originPackageID = originalPackageID;
        view.originPackageScore = score;
        return view;
    }
}
}