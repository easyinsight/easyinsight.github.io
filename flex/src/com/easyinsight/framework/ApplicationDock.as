package com.easyinsight.framework {
import flash.events.Event;

import mx.containers.Canvas;

[Event(name="moduleAnalyze", type="com.easyinsight.genredata.ModuleAnalyzeEvent")]

public class ApplicationDock extends Canvas {

    private var _messageListener:EIMessageListener;

    public function ApplicationDock() {
        super();
        User.getEventNotifier().addEventListener(LoginEvent.LOGIN, login);
        User.getEventNotifier().addEventListener(LoginEvent.LOGOUT, logout);
        this.width = 0;
    }

    private function logout(event:LoginEvent):void {
        this.width = 0;
    }

    private function login(event:LoginEvent):void {
        this.width = 29;
    }

    [Bindable(event="messageListenerChanged")]
    public function get messageListener():EIMessageListener {
        return _messageListener;
    }

    public function set messageListener(value:EIMessageListener):void {
        if (_messageListener == value) return;
        _messageListener = value;
        dispatchEvent(new Event("messageListenerChanged"));
    }
}
}