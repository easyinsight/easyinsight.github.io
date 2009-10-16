package com.easyinsight.outboundnotifications {

import com.easyinsight.framework.AsyncInfoEvent;
import com.easyinsight.framework.CorePageButton;
import com.easyinsight.framework.EIMessageListener;

import com.easyinsight.framework.LoginEvent;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.ModuleAnalyzeEvent;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.events.CloseEvent;

import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

[Event(name="moduleAnalyze", type="com.easyinsight.genredata.ModuleAnalyzeEvent")]
public class BackgroundTaskButton extends CorePageButton{

    private var asyncWindow:AsyncNotifyWindow;
    private var userUploadService:RemoteObject;
    private var analysisService:RemoteObject;
    private var showingWindow:Boolean;

    private var lastX:int;
    private var lastY:int;

    public function BackgroundTaskButton() {
        super();
        userUploadService = new RemoteObject();
        userUploadService.destination = "userUpload";
        userUploadService.getOngoingTasks.addEventListener(ResultEvent.RESULT, tasksRetrieved);
        userUploadService.getOngoingTasks.addEventListener(FaultEvent.FAULT, onFault);
        User.getEventNotifier().addEventListener(LoginEvent.LOGIN, onCreation);
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
            showingWindow = false;
        } else {
            showWindow();
        }

    }

    private function showWindow():void {
        if (asyncWindow == null) {
                asyncWindow = new AsyncNotifyWindow();
                BindingUtils.bindProperty(asyncWindow, "data", this, "asyncData");
                asyncWindow.addEventListener(CloseEvent.CLOSE, onClose);
                asyncWindow.addEventListener(AsyncDeleteEvent.ASYNC_DELETE, onDelete);
                asyncWindow.addEventListener(ModuleAnalyzeEvent.MODULE_ANALYZE, onModuleAnalyze);
            }
            PopUpManager.addPopUp(asyncWindow, this, false);
            asyncWindow.x = lastX;
            asyncWindow.y = lastY;
        showingWindow = true;
    }

    private function onModuleAnalyze(event:ModuleAnalyzeEvent):void {
        dispatchEvent(event.clone());
    }

    private function onDelete(event:AsyncDeleteEvent):void {
        _asyncData.removeItemAt(_asyncData.getItemIndex(event.info));
    }

    private function onClose(event:CloseEvent):void {
        lastX = asyncWindow.x;
        lastY = asyncWindow.y;
        showingWindow = false;
    }

    private function onCreation(event:LoginEvent):void {
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
        if (info.action == RefreshEventInfo.CREATE) {
            _asyncData.addItem(info);
            bringWindowToFront();
        }
        else if (info.action == RefreshEventInfo.ADD) {
            var found:Boolean = false;
            for (var index:int = 0; i < _asyncData.length; i++) {
                var curEvent:RefreshEventInfo = _asyncData.getItemAt(i) as RefreshEventInfo;
                if (curEvent.taskId == info.taskId) {
                    _asyncData.setItemAt(info, i);
                    bringWindowToFront();
                    found = true;
                }
            }
            if(!found)
                _asyncData.addItem(info);
        }
        else {
            for (var i:int = 0; i < _asyncData.length; i++) {
                var currentEvent:RefreshEventInfo = _asyncData.getItemAt(i) as RefreshEventInfo;
                if (currentEvent.taskId == info.taskId) {
                    _asyncData.setItemAt(info, i);
                    bringWindowToFront();
                }
            }
        }
    }


    private function bringWindowToFront():void {
        if(!showingWindow)
            showWindow();
    }


}
}