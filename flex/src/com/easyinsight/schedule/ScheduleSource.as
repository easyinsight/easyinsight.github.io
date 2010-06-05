package com.easyinsight.schedule {
import com.easyinsight.FullScreenPage;
import com.easyinsight.listing.AnalyzeSource;

public class ScheduleSource implements AnalyzeSource {
    public function ScheduleSource() {
    }

    public function createAnalysisPopup():FullScreenPage {
        return new ScheduleManagement();
    }
}
}