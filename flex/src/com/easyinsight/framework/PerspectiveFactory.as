package com.easyinsight.framework {
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.account.AccountBasePage;
import com.easyinsight.account.AccountSetupWizard;
import com.easyinsight.account.NewFreeUserWelcome;
import com.easyinsight.customupload.api.APIPage;
import com.easyinsight.dashboard.DashboardEditor;
import com.easyinsight.dashboard.DashboardView;
import com.easyinsight.etl.LookupTableEditor;
import com.easyinsight.feedassembly.NewCompositeFeedWorkspace;
import com.easyinsight.genredata.GenreDataProvider;
import com.easyinsight.groups.GroupsSummary;
import com.easyinsight.listing.MyData;
import com.easyinsight.listing.SimpleIntro;
import com.easyinsight.report.MultiReportView;
import com.easyinsight.report.MultiScreenView;
import com.easyinsight.report.ReportView;
import com.easyinsight.schedule.ScheduleManagement;
import com.easyinsight.scorecard.LoggedInHome;
import com.easyinsight.solutions.PostInstallPage;
import com.easyinsight.solutions.RevisedSolutionSummary;

public class PerspectiveFactory implements IPerspectiveFactory {
    public function PerspectiveFactory() {
    }

    public function fromType(type:int):PerspectiveFactoryResult {
        var perspective:PerspectiveFactoryResult;
        switch (type) {
            case PerspectiveInfo.REPORT_EDITOR:
                perspective = new DirectUIComponent(new DataAnalysisContainer());
                break;
            case PerspectiveInfo.REPORT_VIEW:
                perspective = new DirectUIComponent(new ReportView());
                break;
            case PerspectiveInfo.MULTI_REPORT_VIEW:
                perspective = new DirectUIComponent(new MultiReportView());
                break;
            case PerspectiveInfo.SPREADSHEET_WIZARD:
                perspective = new ModuleUIComponent("SpreadsheetWizard", "Loading the wizard to process your spreadsheet...");
                break;
            case PerspectiveInfo.COMPOSITE_WORKSPACE:
                perspective = new DirectUIComponent(new NewCompositeFeedWorkspace());
                break;
            case PerspectiveInfo.KPI_TREE_ADMIN:
                perspective = new ModuleUIComponent("KPITreeAdmin", "Loading the KPI tree configuration page...");
                break;
            case PerspectiveInfo.KPI_TREE_VIEW:
                perspective = new ModuleUIComponent("KPITreeView", "Loading the KPI tree view page...");
                break;
            case PerspectiveInfo.DATA_SOURCE_ADMIN:
                perspective = new ModuleUIComponent("DataSourceAdmin", "Loading the data source administration page...");
                break;
            case PerspectiveInfo.POST_CONNECTION_INSTALL:
                perspective = new DirectUIComponent(new PostInstallPage());
                break;
            case PerspectiveInfo.ACCOUNT_CREATION:
                perspective = new DirectUIComponent(new AccountSetupWizard());
                break;
            case PerspectiveInfo.LOOKUP_TABLE:
                perspective = new DirectUIComponent(new LookupTableEditor());
                break;
            case PerspectiveInfo.MULTI_SCREEN_VIEW:
                perspective = new DirectUIComponent(new MultiScreenView());
                break;
            case PerspectiveInfo.HOME_PAGE:
                perspective = new DirectUIComponent(new SimpleIntro());
                break;
            case PerspectiveInfo.SCORECARDS:
                perspective = new DirectUIComponent(new LoggedInHome());
                break;
            case PerspectiveInfo.MY_DATA:
                perspective = new DirectUIComponent(new MyData());
                break;
            case PerspectiveInfo.GROUPS:
                perspective = new DirectUIComponent(new GroupsSummary());
                break;
            case PerspectiveInfo.API:
                perspective = new DirectUIComponent(new APIPage());
                break;
            case PerspectiveInfo.EXCHANGE:
                perspective = new DirectUIComponent(new GenreDataProvider());
                break;
            case PerspectiveInfo.HELP:
                perspective = new DirectUIComponent(new NewFreeUserWelcome());
                break;
            case PerspectiveInfo.ACCOUNT:
                perspective = new DirectUIComponent(new AccountBasePage());
                break;
            case PerspectiveInfo.CONNECTIONS:
                perspective = new DirectUIComponent(new RevisedSolutionSummary());
                break;
            case PerspectiveInfo.GROUP_DETAIL:
                perspective = new ModuleUIComponent("GroupDetail", "Loading the group detail information...");
                break;
            case PerspectiveInfo.CONNECTION_DETAIL:
                perspective = new ModuleUIComponent("ConnectionDetail", "Loading the connection information...");
                break;
            case PerspectiveInfo.KPI_WINDOW:
                perspective = new ModuleUIComponent("KPIModule", "Loading the KPI editor...");
                break;
            case PerspectiveInfo.ANALYSIS_ITEM_EDITOR:
                perspective = new ModuleUIComponent("AnalysisItemEditor", "Loading the field editor...");
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
        }
        return perspective;
    }
}
}