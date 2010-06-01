package com.easyinsight.framework {
import flash.events.Event;
import flash.events.EventDispatcher;

public class UserName extends EventDispatcher {

    private var _userName:String;

    private static var _instance:UserName;

    public function UserName() {
    }

    public static function setup():void {
        _instance = new UserName();
    }

    public static function instance():UserName {
        return _instance;
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