package com.easyinsight.solutions {

import com.easyinsight.account.UpgradeEvent;
import com.easyinsight.administration.feed.BulkFieldWindow;
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.analysis.PromptEvent;
import com.easyinsight.analysis.SavePromptWindow;
import com.easyinsight.customupload.ConfigureDataSource;
import com.easyinsight.customupload.DataSourceConfiguredEvent;
import com.easyinsight.datasources.DataSourceBehavior;
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.listing.IPerspective;
import com.easyinsight.listing.DescriptorAnalyzeSource;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.schedule.DailyScheduleType;
import com.easyinsight.schedule.DataSourceRefreshActivity;
import com.easyinsight.skin.BackgroundImage;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.display.Bitmap;
import flash.display.DisplayObject;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;
import flash.net.FileReference;
import flash.net.URLRequest;
import flash.net.navigateToURL;
import flash.utils.ByteArray;

import mx.containers.Box;

import mx.containers.Canvas;

import mx.controls.Alert;
import mx.core.Application;
import mx.core.UIComponent;
import mx.events.CloseEvent;
import mx.events.FlexEvent;
import mx.managers.BrowserManager;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;
import mx.utils.URLUtil;

[Event(name="install", type="com.easyinsight.solutions.SolutionDetailEvent")]
[Event(name="externalSite", type="com.easyinsight.solutions.SolutionDetailEvent")]

public class SolutionDetailRenderer extends BackgroundImage implements IPerspective {

    private var _solution:Solution;
    private var solutionService:RemoteObject;
    private var installResult:FeedDefinitionData;

    private var _logo:Bitmap;

    public function SolutionDetailRenderer() {
        super();
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.installSolution.addEventListener(ResultEvent.RESULT, installedSolution);
        solutionService.getSolutionArchive.addEventListener(ResultEvent.RESULT, gotSolutionArchive);
        solutionService.connectionInstalled.addEventListener(ResultEvent.RESULT, checkedValidity);
        solutionService.addKPIData.addEventListener(ResultEvent.RESULT, installed);
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        setStyle("verticalAlign", "middle");
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
        percentHeight = 100;
    }

    private function onCreation(event:FlexEvent):void {
        if (_solution != null) {
            var fragmentObject:Object = new Object();
            fragmentObject.solutionID = String(_solution.solutionID);
            var fragmentString:String = URLUtil.objectToString(fragmentObject);
            BrowserManager.getInstance().setFragment(fragmentString);
            BrowserManager.getInstance().setTitle("Easy Insight - " + _solution.name);
        }
        determineInitialState();
    }

    private function determineInitialState():void {
        currentState = "";
        if (User.getInstance().getAccountType() < _solution.solutionTier) {
            toAccountBelowRequiredState();
        } else {
            customInitialState();
        }
    }

    protected function upgrade():void {
        navigateToURL(new URLRequest("/app/billing/accountType.jsp"), "_self");
    }

    private function onUpgrade(event:UpgradeEvent):void {
        determineInitialState();
    }

    protected function customInitialState():void {

    }

    protected function toAccountBelowRequiredState():void {

    }

    protected function toVendorPage():void {
        navigateToURL(new URLRequest(_solution.logoLink), "_blank");
    }

    private function onSourceConfigured(event:DataSourceConfiguredEvent):void {
        if (event.requiresFieldSetup) {
            var bulkFieldWindow:BulkFieldWindow = new BulkFieldWindow();
            bulkFieldWindow.dataSourceID = event.descriptor.id;
            bulkFieldWindow.addEventListener(Event.COMPLETE, onFieldComplete);
            PopUpManager.addPopUp(bulkFieldWindow, DisplayObject(Application.application), true);
            PopUpUtil.centerPopUp(bulkFieldWindow);
        } else {
            connectionInstalled();
        }
    }

    private function onFieldComplete(event:Event):void {
        connectionInstalled();
    }

    private function connectionInstalled():void {
        var kpiData:SolutionKPIData = new SolutionKPIData();
        kpiData.dataSourceID = installResult.dataFeedID;
        if (DataSourceBehavior.pullDataSource(installResult.dataSourceBehavior)) {
            var activity:DataSourceRefreshActivity = new DataSourceRefreshActivity();
            activity.dataSourceID = installResult.dataFeedID;
            activity.dataSourceName = installResult.feedName;
            var schedule:DailyScheduleType = new DailyScheduleType();
            var morningOrEvening:int = int(Math.random() * 2);
            if (morningOrEvening == 0) {
                schedule.hour = int(Math.random() * 6);
            } else {
                schedule.hour = int(Math.random() * 6) + 18;
            }
            schedule.minute = int(Math.random() * 60);
            activity.scheduleType = schedule;
            kpiData.utcOffset = new Date().getTimezoneOffset();
            kpiData.activity = activity;
        }
        kpiData.addDataSourceToGroup = true;
        ProgressAlert.alert(this, "Completing installation...", null, solutionService.addKPIData);
        solutionService.addKPIData.send(kpiData);
    }

    private function installed(event:Event):void {
        dispatchEvent(new NavigationEvent("Home", null, { dataSourceDescriptor: dataSourceDescriptor}));
        PopUpManager.removePopUp(this);
    }

    private var dataSourceDescriptor:DataSourceDescriptor;

    private function installedSolution(event:ResultEvent):void {
        this.installResult = solutionService.installSolution.lastResult as FeedDefinitionData;
        var configuredSources:int = 0;
        for each (var solInstall:SolutionInstallInfo in installResult) {
            if (solInstall.descriptor.getType() == EIDescriptor.DATA_SOURCE && solInstall.requiresConfiguration) {
                configuredSources++;
            }
        }
        var dataSourceDescriptor:DataSourceDescriptor = new DataSourceDescriptor();
        dataSourceDescriptor.id = installResult.dataFeedID;
        dataSourceDescriptor.name = installResult.feedName;
        dataSourceDescriptor.dataSourceType = installResult.getFeedType();
        dataSourceDescriptor.role = 1;
        this.dataSourceDescriptor = dataSourceDescriptor;
        var configWindow:ConfigureDataSource = new ConfigureDataSource();
        configWindow.dataSourceDefinition = installResult;
        configWindow.addEventListener(DataSourceConfiguredEvent.DATA_SOURCE_CONFIGURED, onSourceConfigured, false, 0, true);
        PopUpManager.addPopUp(configWindow, this, true);
        PopUpUtil.centerPopUp(configWindow);
        dispatchEvent(new SolutionEvent(SolutionEvent.SOLUTION_INSTALLED, _solution.solutionID));
    }

    private function checkedValidity(event:ResultEvent):void {
        var installationValidation:InstallationValidation = solutionService.connectionInstalled.lastResult as InstallationValidation;
        if (installationValidation.enterpriseLimit) {
            var eWindow:NewEnterpriseWindow = new NewEnterpriseWindow();
            PopUpManager.addPopUp(eWindow, this, true);
            PopUpUtil.centerPopUp(eWindow);
        } else if (installationValidation.atSizeLimit) {
            var atLimitWindow:SavePromptWindow = new SavePromptWindow();
            atLimitWindow.defineOption("Upgrade Account", PromptEvent.PROMPT_SAVE);
            atLimitWindow.defineOption("Cancel", PromptEvent.PROMPT_CANCEL);
            atLimitWindow.addEventListener(PromptEvent.PROMPT_SAVE, function(event:PromptEvent):void {
                navigateToURL(new URLRequest("/app/billing/accountType.jsp"), "_self");
            });
            atLimitWindow.prompt = "Your account is at its small business connection limit. You'll need to allocate more connections to install this connection.";
            PopUpManager.addPopUp(atLimitWindow, UIComponent(Application.application), true);
            PopUpUtil.centerPopUp(atLimitWindow);
        } else {
            var existingConnectionID:int = solutionService.connectionInstalled.lastResult as int;
            if (existingConnectionID > 0) {
                var window:SavePromptWindow = new SavePromptWindow();
                window.defineOption("Open the Existing Connection", PromptEvent.PROMPT_SAVE);
                window.defineOption("Create New Connection", PromptEvent.PROMPT_DISCARD);
                window.defineOption("Cancel", PromptEvent.PROMPT_CANCEL);
                window.addEventListener(PromptEvent.PROMPT_SAVE, function(event:PromptEvent):void {
                    dispatchEvent(new AnalyzeEvent(new DescriptorAnalyzeSource(existingConnectionID)));
                }, false, 0, true);
                window.addEventListener(PromptEvent.PROMPT_DISCARD, function(event:PromptEvent):void {
                    ProgressAlert.alert(UIComponent(Application.application), "Installing connection...", null, solutionService.installSolution);
                    solutionService.installSolution.send(_solution.solutionID);
                }, false, 0, true);
                window.prompt = "It looks like you already have a data source for this connection under your My Data page. Are you sure you want to install a new data source?";
                PopUpManager.addPopUp(window, UIComponent(Application.application), true);
                PopUpUtil.centerPopUp(window);
            } else {
                ProgressAlert.alert(UIComponent(Application.application), "Installing connection...", null, solutionService.installSolution);
                solutionService.installSolution.send(_solution.solutionID);
            }
        }
    }

    protected function installSolution():void {
        ProgressAlert.alert(this, "Installing connection...", null, solutionService.connectionInstalled);
        solutionService.connectionInstalled.send(_solution.solutionID);
    }

    public function get solution():Solution {
        return _solution;
    }

    public function set solution(value:Solution):void {
        _solution = value;
    }


    [Bindable(event="logoChanged")]
    public function get logo():Bitmap {
        return _logo;
    }

    public function set logo(value:Bitmap):void {
        if (_logo == value) return;
        _logo = value;
        dispatchEvent(new Event("logoChanged"));
    }

    public function gotFocus():void {
    }

    public function cleanup():void {
    }

    private var fileRef:FileReference;

    private function complete(event:Event):void {
        Alert.show("Connection files saved!");
    }

    protected function download():void {
        ProgressAlert.alert(this, "Downloading connection files...", null, solutionService.getSolutionArchive);
        solutionService.getSolutionArchive.send(_solution.solutionID);
    }

    private function gotSolutionArchive(event:ResultEvent):void {
        bytes = solutionService.getSolutionArchive.lastResult as ByteArray;
        var msg:String = "Click to save the connection files.";
        Alert.show(msg, "Alert", Alert.OK | Alert.CANCEL, null, alertListener, null, Alert.CANCEL);
    }

    private var bytes:ByteArray;

    private function alertListener(event:CloseEvent):void {
        if (event.detail == Alert.OK) {
            fileRef = new FileReference();
            fileRef.addEventListener(Event.COMPLETE, complete);
            fileRef.save(bytes, _solution.solutionArchiveName);
        }
    }
}
}