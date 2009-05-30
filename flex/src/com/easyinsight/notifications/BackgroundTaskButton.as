package com.easyinsight.notifications {
import com.easyinsight.framework.AsyncInfoEvent;
import com.easyinsight.framework.CorePageButton;
import com.easyinsight.framework.EIMessageListener;

import com.easyinsight.framework.LoginEvent;

import com.easyinsight.framework.User;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class BackgroundTaskButton extends CorePageButton{

    private var asyncWindow:AsyncNotifyWindow;
    private var userUploadService:RemoteObject;

    public function BackgroundTaskButton() {
        super();
        userUploadService = new RemoteObject();
        userUploadService.destination = "userUpload";
        userUploadService.getOngoingTasks.addEventListener(ResultEvent.RESULT, tasksRetrieved);
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        if (asyncWindow == null) {
            asyncWindow = new AsyncNotifyWindow();
            BindingUtils.bindProperty(asyncWindow, "data", this, "asyncData");
        }
        PopUpManager.addPopUp(asyncWindow, this, false);
    }

    private function onCreation(event:FlexEvent):void {
        User.getEventNotifier().addEventListener(LoginEvent.LOGIN, onLogin);
    }

    private function onLogin(event:LoginEvent):void {        
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