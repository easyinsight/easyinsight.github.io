/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/10/11
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.test {
import com.easyinsight.listing.MyDataTree;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;
import mx.rpc.remoting.mxml.RemoteObject;

public class DataModel extends EventDispatcher {

    private var uploadService:mx.rpc.remoting.RemoteObject;

    private var _reports:ArrayCollection;

    private static var model:DataModel;

    public static function instance():DataModel {
        if (model == null) {
            model = new DataModel();
        }
        return model;
    }

    public function DataModel() {
        uploadService = new mx.rpc.remoting.RemoteObject();
        uploadService.destination = "userUpload";
        uploadService.endpoint = "https://www.easy-insight.com/app/messagebroker/amfsecure";
        uploadService.getFeedAnalysisTree.addEventListener(ResultEvent.RESULT, gotTree);
        uploadService.getFeedAnalysisTree.addEventListener(FaultEvent.FAULT, onFault);
    }

    private function gotTree(event:ResultEvent):void {
        var tree:MyDataTree = uploadService.getFeedAnalysisTree.lastResult as MyDataTree;
        trace("got " + tree.objects.length);
        this.reports = tree.objects;
        dispatchEvent(new Event('gotData'));
    }

    private function onFault(event:FaultEvent):void {
        trace(event.fault.faultString + " - " + event.fault.faultDetail);
    }

    [Bindable(event="reportsChanged")]
    public function get reports():ArrayCollection {
        return _reports;
    }

    public function set reports(value:ArrayCollection):void {
        if (_reports == value) return;
        _reports = value;
        dispatchEvent(new Event("reportsChanged"));
    }

    public function populate():void {
        trace("populating");
        uploadService.getFeedAnalysisTree.send(false);
    }
}
}
