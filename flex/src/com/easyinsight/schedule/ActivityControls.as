package com.easyinsight.schedule {
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.controls.Button;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class ActivityControls extends UIComponent implements IListItemRenderer {

    private var activity:ScheduledActivity;

    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var editButton:Button;

    private var deleteButton:Button;

    private var exportService:RemoteObject;

    public function ActivityControls() {
        super();
    }

    override protected function createChildren():void {
        super.createChildren();
        if (editButton == null) {
            editButton = new Button();
            editButton.setStyle("icon", editIcon);
            editButton.toolTip = "Edit scheduled activity...";
            editButton.addEventListener(MouseEvent.CLICK, editActivity);
        }
        addChild(editButton);
        if (deleteButton == null) {
            deleteButton = new Button();
            deleteButton.setStyle("icon", deleteIcon);
            deleteButton.toolTip = "Delete scheduled activity";
            deleteButton.addEventListener(MouseEvent.CLICK, deleteActivity);
        }
        addChild(deleteButton);
        exportService = new RemoteObject();
        exportService.destination = "exportService";
        exportService.getRefreshableDataSources.addEventListener(ResultEvent.RESULT, onResult);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var buttonWidth:int = 40;
        var buttonHeight:int = 22;
        var padding:int = 5;
        editButton.move((padding),0);
        editButton.setActualSize(buttonWidth, buttonHeight);
        deleteButton.move((padding * 2) + (buttonWidth),0);
        deleteButton.setActualSize(buttonWidth, buttonHeight);
    }

    private function editActivity(event:MouseEvent):void {
        if (activity is DataSourceRefreshActivity) {
            ProgressAlert.alert(this, "Retrieving information...", null, exportService.getRefreshableDataSources);
            exportService.getRefreshableDataSources.send(activity);    
        } else if (activity is ReportDelivery) {
            var window:ReportDeliveryScheduleWindow = new ReportDeliveryScheduleWindow();
            window.activity = activity;
            window.addEventListener(ScheduleActivityEvent.NEW_ACTIVITY, passThrough, false, 0, true);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        } else if (activity is ScorecardDelivery) {
            var scorecardWindow:ScorecardDeliveryScheduleWindow = new ScorecardDeliveryScheduleWindow();
            scorecardWindow.activity = activity;
            scorecardWindow.addEventListener(ScheduleActivityEvent.NEW_ACTIVITY, passThrough, false, 0, true);
            PopUpManager.addPopUp(scorecardWindow, this, true);
            PopUpUtil.centerPopUp(scorecardWindow);
        }
    }

    private function onResult(event:ResultEvent):void {
        var dataSources:ArrayCollection = exportService.getRefreshableDataSources.lastResult as ArrayCollection;
        var window:DataSourceScheduleWindow = new DataSourceScheduleWindow();
        window.activity = activity;
        window.addEventListener(ScheduleActivityEvent.EDIT_ACTIVITY, passThrough, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function passThrough(event:ScheduleActivityEvent):void {
        dispatchEvent(event);
    }

    private function deleteActivity(event:MouseEvent):void {
        dispatchEvent(new ScheduleActivityEvent(ScheduleActivityEvent.DELETE_ACTIVITY, activity));
    }

    [Bindable("dataChange")]
    public function set data(value:Object):void {
        activity = value as ScheduledActivity;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return activity;
    }
}
}