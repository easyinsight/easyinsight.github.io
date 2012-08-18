/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/15/11
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.customupload {
import com.easyinsight.administration.feed.CallData;

import com.easyinsight.administration.feed.CredentialsResponse;
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.analysis.ReportFault;
import com.easyinsight.datasources.DataSourceBehavior;
import com.easyinsight.datasources.IPostOAuth;
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.framework.User;
import com.easyinsight.schedule.DailyScheduleType;
import com.easyinsight.schedule.DataSourceRefreshActivity;
import com.easyinsight.scorecard.DataSourceAsyncEvent;
import com.easyinsight.solutions.DataSourceDescriptor;
import com.easyinsight.solutions.SolutionKPIData;
import com.easyinsight.util.CancelButton;
import com.easyinsight.util.EISlimWindow;
import com.easyinsight.util.ProgressAlert;

import flash.events.Event;
import flash.events.EventDispatcher;

import flash.events.MouseEvent;
import flash.events.TimerEvent;
import flash.utils.Timer;

import mx.containers.VBox;
import mx.controls.Alert;
import mx.controls.Label;
import mx.controls.ProgressBar;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedSync extends EISlimWindow {

    private var asyncLabel:Label;
    private var asyncService:RemoteObject;
    private var callDataID:String;
    private var _dataSourceDefinition:FeedDefinitionData;

    private var progressBar:ProgressBar;

    private var uploadService:RemoteObject;
    private var solutionService:RemoteObject;

    public function set dataSourceDefinition(value:FeedDefinitionData):void {
        _dataSourceDefinition = value;
    }

    public function DelayedSync() {
        uploadService = new RemoteObject();
        uploadService.destination = "userUpload";
        uploadService.completeInstallation.addEventListener(ResultEvent.RESULT, refreshed);
        asyncService = new RemoteObject();
        asyncService.destination = "asyncService";
        asyncService.getCallData.addEventListener(ResultEvent.RESULT, gotCallData);
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.addKPIData.addEventListener(ResultEvent.RESULT, installed);
        this.width = 550;
    }

    private var setupObj:IPostOAuth;

    override protected function createChildren():void {
        super.createChildren();
        if (_dataSourceDefinition.requiresMoreSetupAfterAuth()) {
            setupObj = _dataSourceDefinition.moreSetup();
            EventDispatcher(setupObj).addEventListener(Event.COMPLETE, onSetupDone);
            EventDispatcher(setupObj).addEventListener(Event.CANCEL, onCancel);
            setupObj.retrieve(this);
        } else {
            ProgressAlert.alert(this, "Determining synchronization requirements...", null, uploadService.completeInstallation);
            uploadService.completeInstallation.send(_dataSourceDefinition);
        }
    }

    private function onSetupDone(event:Event):void {
        EventDispatcher(setupObj).removeEventListener(Event.COMPLETE, onSetupDone);
        setupObj = null;
        ProgressAlert.alert(this, "Determining synchronization requirements...", null, uploadService.completeInstallation);
        uploadService.completeInstallation.send(_dataSourceDefinition);
    }

    private function onCancel(event:Event):void {
        if (timer != null) {
            timer.stop();
        }
        PopUpManager.removePopUp(this);
    }

    private var timer:Timer;

    private function onTimer(event:TimerEvent):void {
        asyncService.getCallData.send(callDataID);
    }

    private function refreshed(event:ResultEvent):void {
        var response:CredentialsResponse = uploadService.completeInstallation.lastResult as CredentialsResponse;
        if (response.successful) {
            if (response.callDataID != null) {
                var vbox:VBox = new VBox();
                vbox.setStyle("horizontalAlign", "center");
                progressBar = new ProgressBar();
                progressBar.label = "";
                progressBar.labelPlacement = "top";
                progressBar.width = 400;
                progressBar.indeterminate = true;
                vbox.addChild(progressBar);
                asyncLabel = new Label();
                asyncLabel.maxWidth = 500;
                asyncLabel.text = "Retrieving data from " + _dataSourceDefinition.feedName + ", this may take a few minutes...";
                vbox.addChild(asyncLabel);
                var button:CancelButton = new CancelButton();
                button.label = "Cancel";
                button.addEventListener(MouseEvent.CLICK, onCancel);
                vbox.addChild(button);
                addChild(vbox);
                callDataID = response.callDataID;
                timer = new Timer(5000, 0);
                timer.addEventListener(TimerEvent.TIMER, onTimer);
                timer.start();
            } else {
                connectionInstalled();
            }
        } else {
            progressBar.visible = false;
            Alert.show(response.failureMessage);
        }
    }

    private function connectionInstalled():void {
        var kpiData:SolutionKPIData = new SolutionKPIData();
        kpiData.dataSourceID = _dataSourceDefinition.dataFeedID;
        if (DataSourceBehavior.pullDataSource(_dataSourceDefinition.dataSourceBehavior)) {
            var activity:DataSourceRefreshActivity = new DataSourceRefreshActivity();
            activity.dataSourceID = _dataSourceDefinition.dataFeedID;
            activity.dataSourceName = _dataSourceDefinition.feedName;
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
        var desc:DataSourceDescriptor = new DataSourceDescriptor();
        desc.id = _dataSourceDefinition.dataFeedID;
        desc.name = _dataSourceDefinition.feedName;
        User.getEventNotifier().dispatchEvent(new NavigationEvent("Home", null, { dataSourceDescriptor: desc}));
        PopUpManager.removePopUp(this);
    }

    private function gotCallData(event:ResultEvent):void {
        var callData:CallData = asyncService.getCallData.lastResult as CallData;
        if (callData.status == CallData.DONE) {
            timer.stop();
            connectionInstalled();
        } else if (callData.status == CallData.FAILED) {
            timer.stop();
            progressBar.visible = false;
            PopUpManager.removePopUp(this);
            if (callData.result is String) {
                Alert.show(String(callData.statusMessage));
            } else if (callData.result is ReportFault) {
                var fault:ReportFault = callData.result as ReportFault;
                Alert.show(fault.getMessage());
            } else {
                Alert.show("Something went wrong in trying to retrieve data. Please double check your configuration information.");
            }
        } else if (callData.status == CallData.RUNNING) {
            if (callData.result != null) {
                var scorecardEvent:DataSourceAsyncEvent = callData.result as DataSourceAsyncEvent;
                asyncLabel.text = scorecardEvent.dataSourceName;
            } else {
                asyncLabel.text = "Retrieving data from " + _dataSourceDefinition.feedName + ", this may take a few minutes...";
            }
        }
    }
}
}
