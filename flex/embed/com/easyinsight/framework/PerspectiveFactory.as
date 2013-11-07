package com.easyinsight.framework {
import com.easyinsight.SimpleReportEditor;
import com.easyinsight.account.AccountBasePage;
import com.easyinsight.account.Help;
import com.easyinsight.administration.feed.FeedAdministrationContainer;
import com.easyinsight.analysis.AnalysisItemEditor;
import com.easyinsight.customupload.wizard.SpreadsheetUpdateWizard;
import com.easyinsight.customupload.wizard.SpreadsheetWizard;
import com.easyinsight.dashboard.DashboardEditor;
import com.easyinsight.dashboard.DashboardView;
import com.easyinsight.etl.LookupTableEditor;
import com.easyinsight.feedassembly.NewCompositeFeedWorkspace;
import com.easyinsight.feedassembly.NewFederatedWindow;
import com.easyinsight.genredata.Exchange;
import com.easyinsight.kpi.KPIModule;
import com.easyinsight.listing.DataSourceSpecificList;
import com.easyinsight.listing.MyData;
import com.easyinsight.report.ReportView;
import com.easyinsight.schedule.ScheduleManagement;
import com.easyinsight.scorecard.ScorecardEditor;
import com.easyinsight.scorecard.ScorecardView;
import com.easyinsight.solutions.ConnectionDetail;
import com.easyinsight.solutions.RevisedSolutionSummary;

public class PerspectiveFactory implements IPerspectiveFactory {
    public function PerspectiveFactory() {
    }

    public function fromType(type:int):PerspectiveFactoryResult {
        var perspective:PerspectiveFactoryResult;
        switch (type) {
            case PerspectiveInfo.ANALYSIS_ITEM_EDITOR:
                perspective = new DirectUIComponent(new AnalysisItemEditor());
                break;
        }
        return perspective;
    }
}
}