package com.easyinsight.solutions {
import com.easyinsight.LoginDialog;
import com.easyinsight.account.Account;
import com.easyinsight.customupload.ConfigureDataSource;
import com.easyinsight.customupload.DataSourceConfiguredEvent;
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.goals.GoalDataAnalyzeSource;
import com.easyinsight.goals.GoalTreeDescriptor;
import com.easyinsight.listing.DescriptorAnalyzeSource;
import com.easyinsight.listing.IPerspective;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.report.MultiScreenAnalyzeSource;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.util.FileAlert;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.display.Bitmap;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;
import flash.events.HTTPStatusEvent;
import flash.events.IOErrorEvent;
import flash.events.ProgressEvent;
import flash.events.SecurityErrorEvent;
import flash.net.FileReference;
import flash.net.URLRequest;
import flash.net.URLRequestMethod;
import flash.net.URLVariables;
import flash.net.navigateToURL;

import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.controls.Alert;
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
        if (User.getInstance() == null) {
            toNotLoggedInState();
        } else if (User.getInstance().getAccountType() < _solution.solutionTier) {
            toAccountBelowRequiredState();
        } else {
            customInitialState();
        }
    }


    public function get newAuth():Boolean {
        return _newAuth;
    }

    public function set newAuth(value:Boolean):void {
        _newAuth = value;
    }

    protected function customInitialState():void {

    }

    protected function toNotLoggedInState():void {

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
                if (User.getInstance().getAccountType() >= Account.GROUP) {
                    goalTrees++;
                    items.addItem(solInstall.descriptor);
                }
            } else if (solInstall.descriptor.getType() == EIDescriptor.REPORT) {
                reports++;
                items.addItem(solInstall.descriptor);
            }
        }
        var dataSource:DataSourceDescriptor = dataSourceItems.getItemAt(0) as DataSourceDescriptor;
        var window:PostInstallWindow = new PostInstallWindow();
        window.dataSourceDescriptor = dataSource;
        window.solution = _solution;
        window.addEventListener(AnalyzeEvent.ANALYZE, passThrough);
        window.addEventListener(NavigationEvent.NAVIGATION, passThrough);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
        /*if (items.length == 1 && goalTrees == 1) {
            dispatchEvent(new AnalyzeEvent(new GoalDataAnalyzeSource((GoalTreeDescriptor(items.getItemAt(0)).id))));
        } else if (items.length == 1 && reports == 1) {
            dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(InsightDescriptor(items.getItemAt(0)))));
        } else if (items.length > 1) {
            dispatchEvent(new AnalyzeEvent(new MultiScreenAnalyzeSource(items, _solution)));
        } else {
            var dataSourceDescriptor:DataSourceDescriptor = dataSourceItems.getItemAt(0) as DataSourceDescriptor;
            dispatchEvent(new AnalyzeEvent(new DescriptorAnalyzeSource(dataSourceDescriptor.id, dataSourceDescriptor.name)));
        }*/
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
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
                configWindow.addEventListener(DataSourceConfiguredEvent.DATA_SOURCE_CONFIGURED, onSourceConfigured);
                PopUpManager.addPopUp(configWindow, this, true);
                PopUpUtil.centerPopUp(configWindow);
            }
        }
        if (immediate) {
            postInstall();
            //Alert.show("The solution has been installed. Any data sources and/or reports associated to this solution will now be available to you under the My Data page.");
        }
        dispatchEvent(new SolutionEvent(SolutionEvent.SOLUTION_INSTALLED, _solution.solutionID));
    }

    protected function createAccount():void {
        var urlObject:Object = new Object();
        urlObject.solutionID = _solution.solutionID;
        var url:String = URLUtil.objectToString(urlObject);
        var props:Object = new Object();
        props["destinationURL"] = url;
        User.getEventNotifier().dispatchEvent(new NavigationEvent(NavigationEvent.ACCOUNTS, null, props));
    }

    protected function installSolution():void {
        ProgressAlert.alert(this, "Installing connection...", null, solutionService.installSolution);
        solutionService.installSolution.send(_solution.solutionID);
    }

    protected function login():void {
        var login:LoginDialog = new LoginDialog();
        login.additionalInfo = "You need to be logged in before you can install this connection.";
        var urlObject:Object = new Object();
        urlObject.solutionID = _solution.solutionID;
        login.targetURL = URLUtil.objectToString(urlObject);
        PopUpManager.addPopUp(login, this, true);
        PopUpUtil.centerPopUp(login);
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
        Alert.show("Solution files copied!");
    }

    protected function download():void {
        var request:URLRequest = new URLRequest("/app/DownloadServlet");
        request.method = URLRequestMethod.GET;
        var vars:URLVariables = new URLVariables();

        vars.userName = User.getInstance().userName;
        vars.password = User.getInstance().password;
        vars.operation = String(2);
        vars.fileID = String(_solution.solutionID);
        request.data = vars;

        fileRef = new FileReference();
        fileRef.addEventListener(Event.CANCEL, doEvent);
        fileRef.addEventListener(Event.COMPLETE, complete);
        fileRef.addEventListener(Event.OPEN, doEvent);
        fileRef.addEventListener(Event.SELECT, doEvent);
        fileRef.addEventListener(HTTPStatusEvent.HTTP_STATUS, doEvent);
        fileRef.addEventListener(IOErrorEvent.IO_ERROR, doEvent);
        fileRef.addEventListener(ProgressEvent.PROGRESS, doEvent);
        fileRef.addEventListener(SecurityErrorEvent.SECURITY_ERROR, doEvent);

        FileAlert.alert(this, "Downloading...", null, fileRef);
        fileRef.download(request, _solution.solutionArchiveName);
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