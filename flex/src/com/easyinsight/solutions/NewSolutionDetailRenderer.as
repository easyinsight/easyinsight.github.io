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
import com.easyinsight.listing.DescriptorAnalyzeSource;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.schedule.DailyScheduleType;
import com.easyinsight.schedule.DataSourceRefreshActivity;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.display.DisplayObject;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.controls.Alert;
import mx.core.Application;
import mx.core.UIComponent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

[Event(name="install", type="com.easyinsight.solutions.SolutionDetailEvent")]
[Event(name="externalSite", type="com.easyinsight.solutions.SolutionDetailEvent")]

public class NewSolutionDetailRenderer extends EventDispatcher {

    private var _solution:Solution;
    private var solutionService:RemoteObject;
    private var installResult:FeedDefinitionData;

    public function NewSolutionDetailRenderer() {
        super();
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.installSolution.addEventListener(ResultEvent.RESULT, installedSolution);
        solutionService.connectionInstalled.addEventListener(ResultEvent.RESULT, checkedValidity);
        solutionService.addKPIData.addEventListener(ResultEvent.RESULT, installed);
    }

    protected function upgrade():void {
        navigateToURL(new URLRequest("/app/billing/accountType.jsp"), "_self");
    }

    private function onUpgrade(event:UpgradeEvent):void {
        installSolution();
    }

    private function onSourceConfigured(event:DataSourceConfiguredEvent):void {
        if (event.requiresFieldSetup) {
            var bulkFieldWindow:BulkFieldWindow = new BulkFieldWindow();
            bulkFieldWindow.dataSourceID = event.descriptor.id;
            bulkFieldWindow.addEventListener(Event.COMPLETE, onComplete);
            PopUpManager.addPopUp(bulkFieldWindow, DisplayObject(Application.application), true);
            PopUpUtil.centerPopUp(bulkFieldWindow);
        } else {
            connectionInstalled();
        }
    }

    private function onComplete(event:Event):void {
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
        ProgressAlert.alert(UIComponent(Application.application), "Completing installation...", null, solutionService.addKPIData);
        solutionService.addKPIData.send(kpiData);
    }

    private function installed(event:Event):void {
        User.getEventNotifier().dispatchEvent(new NavigationEvent("Home", null, { dataSourceDescriptor: dataSourceDescriptor}));
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
        PopUpManager.addPopUp(configWindow, UIComponent(Application.application), true);
        PopUpUtil.centerPopUp(configWindow);
        dispatchEvent(new SolutionEvent(SolutionEvent.SOLUTION_INSTALLED, _solution.solutionID));
    }

    private function checkedValidity(event:ResultEvent):void {
        var installationValidation:InstallationValidation = solutionService.connectionInstalled.lastResult as InstallationValidation;
        if (installationValidation.atSizeLimit) {
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
            var renderer:NewSolutionDetailRenderer = this;
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

    public function installSolution():void {
        if (User.getInstance().getAccountType() < _solution.solutionTier) {
            upgrade();
        } else {
            ProgressAlert.alert(UIComponent(Application.application), "Installing connection...", null, solutionService.connectionInstalled);
            solutionService.connectionInstalled.send(_solution.solutionID);
        }
    }

    public function get solution():Solution {
        return _solution;
    }

    public function set solution(value:Solution):void {
        _solution = value;
    }
}
}