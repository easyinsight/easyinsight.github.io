package com.easyinsight.solutions {

import com.easyinsight.account.Account;
import com.easyinsight.account.BasicUpgradeWindow;
import com.easyinsight.account.UpgradeEvent;
import com.easyinsight.customupload.ConfigureDataSource;
import com.easyinsight.customupload.DataSourceConfiguredEvent;
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.listing.IPerspective;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.util.FileAlert;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;
import com.easyinsight.util.UserAudit;

import flash.display.Bitmap;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;
import flash.net.FileReference;
import flash.net.URLRequest;
import flash.net.navigateToURL;
import flash.utils.ByteArray;

import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.events.CloseEvent;
import mx.events.FlexEvent;
import mx.managers.BrowserManager;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;
import mx.utils.URLUtil;

[Event(name="install", type="com.easyinsight.solutions.SolutionDetailEvent")]
[Event(name="externalSite", type="com.easyinsight.solutions.SolutionDetailEvent")]

public class SolutionDetailRenderer extends VBox implements IPerspective {

    private var _solution:Solution;
    private var solutionService:RemoteObject;
    private var installResult:ArrayCollection;
    private var _newAuth:Boolean;

    [Bindable]
    [Embed(source="../../../../assets/background2.JPG")]
    protected var background2:Class;

    private var _logo:Bitmap;

    public function SolutionDetailRenderer() {
        super();
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.installSolution.addEventListener(ResultEvent.RESULT, installedSolution);
        solutionService.getSolutionArchive.addEventListener(ResultEvent.RESULT, gotSolutionArchive);
        solutionService.alreadyHasConnection.addEventListener(ResultEvent.RESULT, checkedValidity);
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
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
        var window:BasicUpgradeWindow = new BasicUpgradeWindow();
        window.addEventListener(UpgradeEvent.UPGRADE_EVENT, onUpgrade, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onUpgrade(event:UpgradeEvent):void {
        determineInitialState();
    }

    public function get newAuth():Boolean {
        return _newAuth;
    }

    public function set newAuth(value:Boolean):void {
        _newAuth = value;
    }

    protected function customInitialState():void {

    }

    protected function toAccountBelowRequiredState():void {

    }

    protected function toVendorPage():void {
        navigateToURL(new URLRequest(_solution.logoLink), "_blank");
    }

    private function onSourceConfigured(event:DataSourceConfiguredEvent):void {
        postInstall();
    }

    private function postInstall():void {
        UserAudit.instance().audit(UserAudit.CONNECTED_TO_DATA);
        var dataSources:int = 0;
        var goalTrees:int = 0;
        var reports:int = 0;
        var items:ArrayCollection = new ArrayCollection();
        var dataSourceItems:ArrayCollection = new ArrayCollection();
        
        for each (var solInstall:SolutionInstallInfo in installResult) {
            if (solInstall.descriptor.getType() == EIDescriptor.DATA_SOURCE) {
                dataSources++;
                dataSourceItems.addItem(solInstall.descriptor);
            } else if (solInstall.descriptor.getType() == EIDescriptor.GOAL_TREE) {
                if (User.getInstance().getAccountType() >= Account.PRO) {
                    goalTrees++;
                    items.addItem(solInstall.descriptor);
                }
            } else if (solInstall.descriptor.getType() == EIDescriptor.REPORT) {
                reports++;
                items.addItem(solInstall.descriptor);
            }
        }
        var dataSource:DataSourceDescriptor = dataSourceItems.getItemAt(0) as DataSourceDescriptor;        
        dispatchEvent(new AnalyzeEvent(new PostInstallSource(dataSource, _solution)));
    }

    private function installedSolution(event:ResultEvent):void {
        this.installResult = solutionService.installSolution.lastResult as ArrayCollection;
        var configuredSources:int = 0;
        for each (var solInstall:SolutionInstallInfo in installResult) {
            if (solInstall.descriptor.getType() == EIDescriptor.DATA_SOURCE && solInstall.requiresConfiguration) {
                configuredSources++;
            }
        }
        var immediate:Boolean = true;
        for each (var solutionInstallInfo:SolutionInstallInfo in installResult) {
            if (solutionInstallInfo.descriptor.getType() == EIDescriptor.DATA_SOURCE && solutionInstallInfo.requiresConfiguration) {
                immediate = false;
                var configWindow:ConfigureDataSource = new ConfigureDataSource();
                configWindow.dataSourceID = solutionInstallInfo.descriptor.id;
                configWindow.onlyDataSource = configuredSources == 1;
                configWindow.addEventListener(DataSourceConfiguredEvent.DATA_SOURCE_CONFIGURED, onSourceConfigured, false, 0, true);
                PopUpManager.addPopUp(configWindow, this, true);
                PopUpUtil.centerPopUp(configWindow);
            }
        }
        if (immediate) {
            postInstall();
        }
        dispatchEvent(new SolutionEvent(SolutionEvent.SOLUTION_INSTALLED, _solution.solutionID));
    }

    protected function createAccount():void {
        var urlObject:Object = new Object();
        urlObject.solutionID = _solution.solutionID;
        var url:String = URLUtil.objectToString(urlObject);
        var props:Object = new Object();
        props["destinationURL"] = url;
        props["requiredTier"] = _solution.solutionTier;
        props["connectionName"] = _solution.name;
        User.getEventNotifier().dispatchEvent(new NavigationEvent(NavigationEvent.ACCOUNTS, null, props));
    }

    private function checkedValidity(event:ResultEvent):void {
        var alreadyHasConnection:Boolean = solutionService.alreadyHasConnection.lastResult as Boolean;
        if (alreadyHasConnection) {
            Alert.show("It looks like you already have a data source for this connection under your My Data page. Are you sure you want to install a new data source?",
                    "Before we go ahead...", Alert.OK | Alert.CANCEL, this, goAheadAndInstall, null, Alert.CANCEL);
        } else {
            ProgressAlert.alert(this, "Installing connection...", null, solutionService.installSolution);
            solutionService.installSolution.send(_solution.solutionID);
        }
    }

    private function goAheadAndInstall(event:CloseEvent):void {
        if (event.detail == Alert.OK) {
            ProgressAlert.alert(this, "Installing connection...", null, solutionService.installSolution);
            solutionService.installSolution.send(_solution.solutionID);
        }
    }

    protected function installSolution():void {
        ProgressAlert.alert(this, "Installing connection...", null, solutionService.alreadyHasConnection);
        solutionService.alreadyHasConnection.send(_solution.solutionID);
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

    private function doEvent(event:Event):void {
        trace(event);
    }

    protected override function commitProperties():void {
        super.commitProperties();
        if (solution != null && solution.image != null) {
            var loader:Loader = new Loader();
            loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete);
            loader.loadBytes(solution.image);
        }
    }

    private function onComplete(event:Event):void {
        var loaderContent:LoaderInfo = event.currentTarget as LoaderInfo;
        logo = Bitmap(loaderContent.loader.content);
        loaderContent.loader.removeEventListener(Event.COMPLETE, onComplete);
    }
}
}