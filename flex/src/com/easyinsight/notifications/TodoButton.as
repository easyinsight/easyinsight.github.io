package com.easyinsight.notifications {
import com.easyinsight.framework.CorePageButton;
import com.easyinsight.framework.EIMessageListener;


import com.easyinsight.framework.TodoInfoEvent;
import flash.events.Event;

import flash.events.MouseEvent;

import flash.utils.describeType;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.events.CloseEvent;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class TodoButton extends CorePageButton{

    private var asyncWindow:TodoNotifyWindow;
    private var userUploadService:RemoteObject;

    private var showingWindow:Boolean;
    private var lastX:int;
    private var lastY:int;

    public function TodoButton() {
        super();
        userUploadService = new RemoteObject();
        userUploadService.destination = "userUpload";
        userUploadService.getTodoEvents.addEventListener(ResultEvent.RESULT, tasksRetrieved);
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onTodoDelete(event:TodoDeleteEvent):void {
        _asyncData.removeItemAt(_asyncData.getItemIndex(event.todoItem));
        userUploadService.deleteTodo.send(event.todoItem.todoID);
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
                asyncWindow = new TodoNotifyWindow();
                BindingUtils.bindProperty(asyncWindow, "data", this, "asyncData");
                asyncWindow.addEventListener(CloseEvent.CLOSE, onClose);
                asyncWindow.addEventListener(TodoDeleteEvent.TODO_DELETE, onTodoDelete);
            }
            PopUpManager.addPopUp(asyncWindow, this, false);
            asyncWindow.x = lastX;
        
            asyncWindow.y = lastY;
        showingWindow = true;
    }

    private function onClose(event:CloseEvent):void {
        lastX = asyncWindow.x;
        lastY = asyncWindow.y;
        showingWindow = false;
    }

    protected function onCreation(event:FlexEvent):void {
        userUploadService.getTodoEvents.send();
    }

    private function hack():void {
        var hack:ConfigureDataFeedInfo;
        var hacka:BuyOurStuffInfo;
    }

    [Bindable]
    private var _asyncData:ArrayCollection = new ArrayCollection();

    private function tasksRetrieved(event:ResultEvent):void {
        _asyncData = userUploadService.getTodoEvents.lastResult as ArrayCollection;
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
        if (info.action == TodoEventInfo.ADD) {
            _asyncData.addItem(info);
            bringWindowToFront();
        }
        else {
            for (var i:int = 0; i < _asyncData.length; i++) {

                var currentEvent:TodoEventInfo = _asyncData.getItemAt(i) as TodoEventInfo;
                if (currentEvent.todoID == info.todoID) {
                    _asyncData.setItemAt(info, i);
                    bringWindowToFront();
                }
            }
        }
    }

    private function bringWindowToFront():void {
        if(!showingWindow) {
            showWindow();
        }
    }
}
}