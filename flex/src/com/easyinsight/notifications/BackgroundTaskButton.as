package com.easyinsight.notifications {
import com.easyinsight.framework.AsyncInfoEvent;
import com.easyinsight.framework.CorePageButton;
import com.easyinsight.framework.EIMessageListener;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class BackgroundTaskButton extends CorePageButton{

    private var asyncWindow:AsyncNotifyWindow;
    private var userUploadService:RemoteObject;
    private var showingWindow:Boolean;

    private var lastX:int;
    private var lastY:int;

    public function BackgroundTaskButton() {
        super();
        userUploadService = new RemoteObject();
        userUploadService.destination = "userUpload";
        userUploadService.getOngoingTasks.addEventListener(ResultEvent.RESULT, tasksRetrieved);
        userUploadService.getOngoingTasks.addEventListener(FaultEvent.FAULT, onFault);
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onFault(event:FaultEvent):void {
        Alert.show(event.fault.message);
    }

    private function onClick(event:MouseEvent):void {
        if (showingWindow) {
            lastX = asyncWindow.x;
            lastY = asyncWindow.y;
            PopUpManager.removePopUp(asyncWindow);
        } else {
            if (asyncWindow == null) {
                asyncWindow = new AsyncNotifyWindow();
                BindingUtils.bindProperty(asyncWindow, "data", this, "asyncData");
            }
            PopUpManager.addPopUp(asyncWindow, this, false);
            asyncWindow.x = lastX;
            asyncWindow.y = lastY;
        }
        showingWindow = !showingWindow;
    }

    private function onCreation(event:FlexEvent):void {
        userUploadService.getOngoingTasks.send();
    }

    [Bindable]
    private var _asyncData:ArrayCollection;

    private function tasksRetrieved(event:ResultEvent):void {
        _asyncData = userUploadService.getOngoingTasks.lastResult as ArrayCollection;
    }

    [Bindable(event="_asyncDataChanged")]
    public function get asyncData():ArrayCollection {
        return _asyncData;
    }

    public function set asyncData(val:ArrayCollection):void {
        _asyncData = val;
        dispatchEvent(new Event("_asyncDataChanged"));
    }

    override protected function onMessageListener(value:EIMessageListener):void {
        value.addEventListener(AsyncInfoEvent.ASYNC_INFO, onAsync);
    }

    private function onAsync(event:AsyncInfoEvent):void {
        var info:RefreshEventInfo = event.info;
        if (info.action == RefreshEventInfo.ADD)
            _asyncData.addItem(info);
        else {
            for (var i:int = 0; i < _asyncData.length; i++) {
                var currentEvent:RefreshEventInfo = _asyncData.getItemAt(i) as RefreshEventInfo;
                if (currentEvent.taskId == info.taskId) {
                    _asyncData.setItemAt(info, i);
                }
            }
        }
    }
}
}