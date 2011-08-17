/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/8/11
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;

import flash.events.TimerEvent;
import flash.utils.Timer;

import mx.core.Application;

import mx.managers.PopUpManager;

import mx.rpc.events.ResultEvent;

import mx.rpc.remoting.RemoteObject;

public class EIGlobalTimer {

    private var timer:Timer;
    private var userService:RemoteObject;
    private var version:String;

    public function EIGlobalTimer() {
    }

    public function start():void {
        userService = new RemoteObject();
        userService.destination = "login";
        userService.getBuildPath.addEventListener(ResultEvent.RESULT, onResult);
        timer = new Timer(360000, 0);
        timer.addEventListener(TimerEvent.TIMER, onTimer);
        timer.start();
    }

    private function onResult(event:ResultEvent):void {
        var basicInfo:BasicInfo = userService.getBuildPath.lastResult as BasicInfo;
        var newVersion:String = basicInfo.version;
        if (version != null && newVersion != version) {
            version = newVersion;
            var window:NewVersionWindow = new NewVersionWindow();
            PopUpManager.addPopUp(window, DisplayObject(Application.application));
            PopUpUtil.centerPopUpWithY(window, 10);
        }
        version = newVersion;
    }

    private function onTimer(event:TimerEvent):void {
        userService.getBuildPath.send();
    }
}
}
