package com.easyinsight.notifications {
import com.easyinsight.framework.CorePageButton;

import com.easyinsight.framework.EIMessageListener;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.events.CloseEvent;
import mx.managers.PopUpManager;

public class NotificationsButton extends CorePageButton{
    private var showingWindow:Boolean;
    private var asyncWindow:ServerNotificationsWindow;

    private var lastX:int;
    private var lastY:int;

    public function NotificationsButton() {
        super();
        showingWindow = false;
        addEventListener(MouseEvent.CLICK, onClick);
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

    private function onClose(event:CloseEvent):void {
        lastX = asyncWindow.x;
        lastY = asyncWindow.y;
        showingWindow = false;
    }

    [Bindable]
    private var _asyncData:ArrayCollection = new ArrayCollection();

    [Bindable(event="_asyncDataChanged")]
    public function get asyncData():ArrayCollection {
        return _asyncData;
    }

    public function set asyncData(val:ArrayCollection):void {
        _asyncData = val;
        dispatchEvent(new Event("_asyncDataChanged"));
    }

    private function bringWindowToFront():void {
        if(!showingWindow) {
            showWindow();
        }
    }

    private function onBroadcastDelete(event:BroadcastDeleteEvent):void {
        _asyncData.removeItemAt(_asyncData.getItemIndex(event.broadcastItem));
    }

    private function showWindow():void {
        if (asyncWindow == null) {
                asyncWindow = new ServerNotificationsWindow();
                BindingUtils.bindProperty(asyncWindow, "data", this, "asyncData");
                asyncWindow.addEventListener(CloseEvent.CLOSE, onClose);
                asyncWindow.addEventListener(BroadcastDeleteEvent.BROADCAST_DELETE, onBroadcastDelete);
            }
            PopUpManager.addPopUp(asyncWindow, this, false);
            asyncWindow.x = lastX;
            asyncWindow.y = lastY;
        showingWindow = true;
    }

    override protected function onMessageListener(value:EIMessageListener):void {
        value.addEventListener(BroadcastInfoEvent.BROADCAST_INFO, onAsync);
    }

    private function onAsync(event:BroadcastInfoEvent):void {
        var info:BroadcastInfo = event.info;
        _asyncData.addItem(info);
        bringWindowToFront();
    }

}
}