/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/8/11
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
import com.easyinsight.framework.BasicInfo;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
import flash.events.EventDispatcher;

import flash.events.TimerEvent;
import flash.utils.Timer;

import mx.core.Application;

import mx.managers.PopUpManager;

import mx.rpc.events.ResultEvent;

import mx.rpc.remoting.RemoteObject;

public class EIGlobalTimer extends EventDispatcher {

    private var timer:Timer;
    private var userService:RemoteObject;
    private var version:String;
    
    public var dataSourceID:int;
    public var dataSourceTime:Date;

    private static var globalTimer:EIGlobalTimer;

    public function EIGlobalTimer() {
    }

    public static function assign(timer:EIGlobalTimer):void {
        globalTimer = timer;
    }

    public static function instance():EIGlobalTimer {
        return globalTimer;
    }

    public function start():void {
        userService = new RemoteObject();
        userService.destination = "login";
        userService.runTimer.addEventListener(ResultEvent.RESULT, onResult);
        timer = new Timer(360000, 0);
        timer.addEventListener(TimerEvent.TIMER, onTimer);
        timer.start();
    }

    private function onResult(event:ResultEvent):void {
        var timerResponse:TimerResponse = userService.runTimer.lastResult as TimerResponse;
        var basicInfo:BasicInfo = timerResponse.basicInfo;
        var newVersion:String = basicInfo.version;
        if (version != null && newVersion != version) {
            version = newVersion;
            var window:NewVersionWindow = new NewVersionWindow();
            PopUpManager.addPopUp(window, DisplayObject(Application.application));
            PopUpUtil.centerPopUpWithY(window, 10);
        }
        version = newVersion;
        if (timerResponse.rerunReport) {
            dispatchEvent(new GlobalDataSourceEvent());
        }
    }

    private function onTimer(event:TimerEvent):void {
        var req:TimerRequest = new TimerRequest();
        req.dataSourceID = dataSourceID;
        req.date = dataSourceTime;
        userService.runTimer.send(req);
    }
}
}
