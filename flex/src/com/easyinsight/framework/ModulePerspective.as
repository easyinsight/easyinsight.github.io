package com.easyinsight.framework {
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.account.AccountBasePage;
import com.easyinsight.account.AccountSetupWizard;
import com.easyinsight.account.NewFreeUserWelcome;
import com.easyinsight.analysis.AnalysisCloseEvent;
import com.easyinsight.analysis.LoadingModuleDisplay;
import com.easyinsight.customupload.api.APIPage;
import com.easyinsight.etl.LookupTableEditor;
import com.easyinsight.feedassembly.NewCompositeFeedWorkspace;
import com.easyinsight.genredata.GenreDataProvider;
import com.easyinsight.groups.GroupDetail;
import com.easyinsight.groups.GroupsSummary;
import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.listing.IModulePerspective;
import com.easyinsight.listing.IPerspective;
import com.easyinsight.listing.MyData;
import com.easyinsight.listing.SimpleIntro;
import com.easyinsight.report.MultiReportView;
import com.easyinsight.report.MultiScreenView;
import com.easyinsight.report.ReportPackageView;
import com.easyinsight.report.ReportView;
import com.easyinsight.scorecard.LoggedInHome;
import com.easyinsight.solutions.PostInstallPage;
import com.easyinsight.solutions.RevisedSolutionSummary;

import flash.display.DisplayObject;
import flash.events.Event;
import flash.events.EventDispatcher;

import mx.containers.Canvas;
import mx.controls.Alert;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class ModulePerspective extends Canvas implements IPerspective, IModulePerspective, AnalyzeSource {


    private var perspectiveType:int;
    private var actualModule:IPerspective;

    private var properties:Object;

    public function ModulePerspective(perspectiveInfo:PerspectiveInfo) {
        percentWidth = 100;
        percentHeight = 100;
        this.perspectiveType = perspectiveInfo.perspectiveType;
        this.properties = perspectiveInfo.properties;
    }

    private function fromPerspectiveType(type:int):void {
        switch (type) {
            case PerspectiveInfo.REPORT_EDITOR:
                inline(new DataAnalysisContainer());
                break;
            case PerspectiveInfo.REPORT_VIEW:
                inline(new ReportView());
                break;
            case PerspectiveInfo.MULTI_REPORT_VIEW:
                inline(new MultiReportView());
                break;
            case PerspectiveInfo.SPREADSHEET_WIZARD:
                loadModule("SpreadsheetWizard", "Loading the wizard to process your spreadsheet...");
                break;
            case PerspectiveInfo.COMPOSITE_WORKSPACE:
                inline(new NewCompositeFeedWorkspace());
                break;
            case PerspectiveInfo.KPI_TREE_ADMIN:
                loadModule("KPITreeAdmin", "Loading the KPI tree configuration page...");
                break;
            case PerspectiveInfo.KPI_TREE_VIEW:
                loadModule("KPITreeView", "Loading the KPI tree view page...");
                break;
            case PerspectiveInfo.DATA_SOURCE_ADMIN:
                loadModule("DataSourceAdmin", "Loading the data source administration page...");
                break;
            case PerspectiveInfo.POST_CONNECTION_INSTALL:
                inline(new PostInstallPage());
                break;
            case PerspectiveInfo.PACKAGE:
                inline(new ReportPackageView());
                break;
            case PerspectiveInfo.ACCOUNT_CREATION:
                inline(new AccountSetupWizard());
                break;
            case PerspectiveInfo.LOOKUP_TABLE:
                inline(new LookupTableEditor());
                break;
            case PerspectiveInfo.MULTI_SCREEN_VIEW:
                inline(new MultiScreenView());
                break;
            case PerspectiveInfo.HOME_PAGE:
                inline(new SimpleIntro());
                break;
            case PerspectiveInfo.SCORECARDS:
                inline(new LoggedInHome());
                break;
            case PerspectiveInfo.MY_DATA:
                inline(new MyData());
                break;
            case PerspectiveInfo.GROUPS:
                inline(new GroupsSummary());
                break;
            case PerspectiveInfo.API:
                inline(new APIPage());
                break;
            case PerspectiveInfo.EXCHANGE:
                inline(new GenreDataProvider());
                break;
            case PerspectiveInfo.HELP:
                inline(new NewFreeUserWelcome());
                break;
            case PerspectiveInfo.ACCOUNT:
                inline(new AccountBasePage());
                break;
            case PerspectiveInfo.CONNECTIONS:
                inline(new RevisedSolutionSummary());
                break;
            case PerspectiveInfo.GROUP_DETAIL:
                inline(new GroupDetail());
                break;
        }
    }

    private function inline(obj:Object):void {
        if (properties != null) {
            for (var p:String in properties) {
                obj[p] = properties[p];
            }
        }
        addListeners(obj as EventDispatcher);
        addChild(obj as DisplayObject);
        actualModule = obj as IPerspective;
    }

    private var moduleInfo:IModuleInfo;

    protected override function createChildren():void {
        super.createChildren();
        fromPerspectiveType(perspectiveType);
    }

    private function loadModule(name:String, message:String):void {
        moduleInfo = ModuleManager.getModule("/app/easyui-debug/" + name + ".swf");
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler, false, 0, true);
        moduleInfo.addEventListener(ModuleEvent.ERROR, moduleFailHandler, false, 0, true);
        _loadingDisplay = new LoadingModuleDisplay();
        _loadingDisplay.message = message;
        _loadingDisplay.moduleInfo = moduleInfo;
        addChild(_loadingDisplay);
        moduleInfo.load();
    }

    private function addListeners(eventDispatcher:EventDispatcher):void {
        eventDispatcher.addEventListener(AnalysisCloseEvent.ANALYSIS_CLOSE, passThrough, false, 0, true);
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    private var _loadingDisplay:LoadingModuleDisplay;

    private function moduleFailHandler(event:ModuleEvent):void {
        Alert.show("Module loading problem - " + event.errorText);
    }

    public function gotFocus():void {
        if (actualModule != null) {
            actualModule.gotFocus();
        }
    }

    public function cleanup():void {
        if (actualModule != null) {
            actualModule.cleanup();
        }
    }

    private function reportLoadHandler(event:ModuleEvent):void {
        if (moduleInfo != null) {
            moduleInfo.removeEventListener(ModuleEvent.READY, reportLoadHandler);
            moduleInfo.removeEventListener(ModuleEvent.ERROR, moduleFailHandler);
            actualModule = moduleInfo.factory.create() as IPerspective;
        }
        if (properties != null) {
            for (var p:String in properties) {
                actualModule[p] = properties[p];
            }
        }
        if (_loadingDisplay != null) {
            removeChild(_loadingDisplay);
            _loadingDisplay.moduleInfo = null;
            _loadingDisplay = null;
        }
        addListeners(actualModule as EventDispatcher);
        addChild(actualModule as DisplayObject);
        actualModule.gotFocus();
    }

    public function createAnalysisPopup():IPerspective {
        return this;
    }
}
}