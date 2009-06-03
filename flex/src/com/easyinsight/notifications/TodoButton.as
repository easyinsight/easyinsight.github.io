package com.easyinsight.notifications {
import com.easyinsight.framework.AsyncInfoEvent;
import com.easyinsight.framework.CorePageButton;
import com.easyinsight.framework.EIMessageListener;

import com.easyinsight.framework.LoginEvent;

import com.easyinsight.framework.TodoInfoEvent;
import com.easyinsight.framework.User;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class TodoButton extends CorePageButton{

    private var asyncWindow:TodoNotifyWindow;
    private var userUploadService:RemoteObject;

    public function TodoButton() {
        super();
        userUploadService = new RemoteObject();
        userUploadService.destination = "userUpload";
        userUploadService.getTodoEvents.addEventListener(ResultEvent.RESULT, tasksRetrieved);
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        addEventListener(MouseEvent.CLICK, onClick);

    }

    private function onClick(event:MouseEvent):void {
        if (asyncWindow == null) {
            asyncWindow = new TodoNotifyWindow();
            BindingUtils.bindProperty(asyncWindow, "data", this, "asyncData");
        }
        PopUpManager.addPopUp(asyncWindow, this, false);
    }

    private function onCreation(event:FlexEvent):void {
        userUploadService.getTodoEvents.send();
    }

    [Bindable]
    private var _asyncData:ArrayCollection;

    private function tasksRetrieved(event:ResultEvent):void {
        _asyncData = userUploadService.getTodoEvents.lastResult as ArrayCollection;
        Alert.show('hi');
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
        value.addEventListener(TodoInfoEvent.TODO_INFO, onAsync);
    }                                                              

    private function onAsync(event:TodoInfoEvent):void {
        var info:TodoEventInfo = event.info;
        if (info.action == TodoEventInfo.ADD)
            _asyncData.addItem(info);
        else {
            for (var i:int = 0; i < _asyncData.length; i++) {
                var currentEvent:TodoEventInfo = _asyncData.getItemAt(i) as TodoEventInfo;
                if (currentEvent.todoID == info.todoID) {
                    _asyncData.setItemAt(info, i);
                }
            }
        }
    }
}
}