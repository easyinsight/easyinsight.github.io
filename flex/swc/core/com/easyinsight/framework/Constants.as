package com.easyinsight.framework {
import flash.events.Event;
import flash.events.EventDispatcher;

public class Constants extends EventDispatcher {

    private var _userName:String;
    private var _buildPath:String;

    private static var _instance:Constants;

    public function Constants() {
    }

    public static function setup():void {
        _instance = new Constants();
    }

    public static function instance():Constants {
        return _instance;
    }

    public function get buildPath():String {
        return _buildPath;
    }

    public function set buildPath(value:String):void {
        _buildPath = value;
    }

    [Bindable(event="userNameChanged")]
    public function get userName():String {
        return _userName;
    }

    public function set userName(value:String):void {
        if (_userName == value) return;
        _userName = value;
        dispatchEvent(new Event("userNameChanged"));
    }
}
}