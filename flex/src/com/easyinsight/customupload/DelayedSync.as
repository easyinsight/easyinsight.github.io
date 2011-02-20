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
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.scorecard.DataSourceAsyncEvent;
import com.easyinsight.solutions.DataSourceDescriptor;

import com.easyinsight.solutions.PostInstallSource;
import com.easyinsight.util.CancelButton;
import com.easyinsight.util.EISlimWindow;
import com.easyinsight.util.ProgressAlert;
import com.easyinsight.util.UserAudit;

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

    public function set dataSourceDefinition(value:FeedDefinitionData):void {
        _dataSourceDefinition = value;
    }

    public function DelayedSync() {
        uploadService = new RemoteObject();
        uploadService.destination = "userUpload";
        uploadService.refreshData.addEventListener(ResultEvent.RESULT, refreshed);
        asyncService = new RemoteObject();
        asyncService.destination = "asyncService";
        asyncService.getCallData.addEventListener(ResultEvent.RESULT, gotCallData);
        this.width = 550;
    }

    override protected function createChildren():void {
        super.createChildren();
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
        ProgressAlert.alert(this, "Starting to synchronize data...", null, uploadService.refreshData);
        uploadService.refreshData.send(_dataSourceDefinition.dataFeedID);
    }

    private function onCancel(event:MouseEvent):void {
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
        var response:CredentialsResponse = uploadService.refreshData.lastResult as CredentialsResponse;
        if (response.successful) {
            if (response.callDataID != null) {
                callDataID = response.callDataID;
                timer = new Timer(5000, 0);
                timer.addEventListener(TimerEvent.TIMER, onTimer);
                timer.start();
            } else {
                var descriptor:DataSourceDescriptor = new DataSourceDescriptor();
                descriptor.id = _dataSourceDefinition.dataFeedID;
                descriptor.name = _dataSourceDefinition.feedName;
                dispatchEvent(new AnalyzeEvent(new PostInstallSource(descriptor)));
                PopUpManager.removePopUp(this);
            }
        } else {
            progressBar.visible = false;
            Alert.show(response.failureMessage);
        }
    }

    private function gotCallData(event:ResultEvent):void {
        var callData:CallData = asyncService.getCallData.lastResult as CallData;
        if (callData.status == CallData.DONE) {
            timer.stop();
            var descriptor:DataSourceDescriptor = new DataSourceDescriptor();
            descriptor.id = _dataSourceDefinition.dataFeedID;
            descriptor.name = _dataSourceDefinition.feedName;
            dispatchEvent(new AnalyzeEvent(new PostInstallSource(descriptor)));
            UserAudit.instance().audit(UserAudit.CONNECTED_TO_DATA);
            PopUpManager.removePopUp(this);
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
                Alert.show("Something went wrong in trying to retrive data. Please double check your configuration information.");
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
