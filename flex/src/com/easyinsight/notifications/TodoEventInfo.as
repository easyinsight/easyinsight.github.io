package com.easyinsight.notifications {
import com.easyinsight.customupload.ConfigureDataSource;

import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
import flash.events.MouseEvent;

import mx.managers.PopUpManager;

[Bindable]
[RemoteClass(alias="com.easyinsight.notifications.TodoEventInfo")]
public class TodoEventInfo extends OutboundEvent{

    public static var ADD:int  = 1;
    public static var COMPLETE:int = 2;
    public static var ERROR:int = 3;

    public var todoID:int;
    public var action:int;
    public var displayObject:DisplayObject;

    protected var _canDelete:Boolean = false;

    public function TodoEventInfo() {
        super();
    }

    public function getTitle():String {
        return "";
    }

    public function canDelete():Boolean {
        return _canDelete;
    }

    public function get title():String {
        return getTitle(); 
    }

    public function onNavigateClick(event:MouseEvent):void {
    }}
}