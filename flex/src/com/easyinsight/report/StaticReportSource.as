package com.easyinsight.report {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;

public class StaticReportSource implements AnalyzeSource {

    private var reportID:int;

    public function StaticReportSource(reportID:int) {
        super();
        this.reportID = reportID;
    }

    public function createAnalysisPopup():FullScreenPage {
        var report:ConnectionStaticReport = new ConnectionStaticReport();
        report.reportID = reportID;
        return report;
    }
}
}