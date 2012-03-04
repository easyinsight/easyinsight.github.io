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
            case PerspectiveInfo.REPORT_EDITOR:
                perspective = new DirectUIComponent(new SimpleReportEditor());
                break;
            case PerspectiveInfo.REPORT_VIEW:
                perspective = new DirectUIComponent(new ReportView());
                break;
            case PerspectiveInfo.SPREADSHEET_WIZARD:
                perspective = new DirectUIComponent(new SpreadsheetWizard());
                break;
            case PerspectiveInfo.SPREADSHEET_UPDATE:
                perspective = new DirectUIComponent(new SpreadsheetUpdateWizard());
                break;
            case PerspectiveInfo.COMPOSITE_WORKSPACE:
                perspective = new DirectUIComponent(new NewCompositeFeedWorkspace());
                break;
            case PerspectiveInfo.DATA_SOURCE_ADMIN:
                perspective = new DirectUIComponent(new FeedAdministrationContainer());
                break;
            case PerspectiveInfo.LOOKUP_TABLE:
                perspective = new DirectUIComponent(new LookupTableEditor());
                break;
            case PerspectiveInfo.HOME_PAGE:
                perspective = new DirectUIComponent(new MyData());
                break;
            case PerspectiveInfo.EXCHANGE:
                perspective = new DirectUIComponent(new Exchange());
                break;
            case PerspectiveInfo.HELP:
                perspective = new DirectUIComponent(new Help());
                break;
            case PerspectiveInfo.ACCOUNT:
                perspective = new DirectUIComponent(new AccountBasePage());
                break;
            case PerspectiveInfo.CONNECTIONS:
                perspective = new DirectUIComponent(new RevisedSolutionSummary());
                break;
            case PerspectiveInfo.CONNECTION_DETAIL:
                perspective = new DirectUIComponent(new ConnectionDetail());
                break;
            case PerspectiveInfo.KPI_WINDOW:
                perspective = new DirectUIComponent(new KPIModule());
                break;
            case PerspectiveInfo.ANALYSIS_ITEM_EDITOR:
                perspective = new DirectUIComponent(new AnalysisItemEditor());
                break;
            case PerspectiveInfo.DASHBOARD_EDITOR:
                perspective = new DirectUIComponent(new DashboardEditor());
                break;
            case PerspectiveInfo.DASHBOARD_VIEW:
                perspective = new DirectUIComponent(new DashboardView());
                break;
            case PerspectiveInfo.SCHEDULING:
                perspective = new DirectUIComponent(new ScheduleManagement());
                break;
            case PerspectiveInfo.SCORECARD_EDITOR:
                perspective = new DirectUIComponent(new ScorecardEditor());
                break;
            case PerspectiveInfo.SCORECARD_VIEW:
                perspective = new DirectUIComponent(new ScorecardView());
                break;
            case PerspectiveInfo.FEDERATED_EDITOR:
                perspective = new DirectUIComponent(new NewFederatedWindow());
                break;
            case PerspectiveInfo.DATA_SOURCE_SPECIFIC:
                perspective = new DirectUIComponent(new DataSourceSpecificList());
                break;
        }
        return perspective;
    }
}
}
