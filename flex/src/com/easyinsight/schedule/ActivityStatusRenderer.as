/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/27/11
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;

import mx.containers.HBox;
import mx.controls.Button;

public class ActivityStatusRenderer extends HBox {
    
    [Embed(source="../../../../assets/sign_warning_16.png")]
    private var warningIcon:Class;
    
    private var button:Button;
    
    public function ActivityStatusRenderer() {
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    override protected function createChildren():void {
        super.createChildren();
        if (!button) {
            button = new Button();
            button.setStyle("icon", warningIcon);
            BindingUtils.bindProperty(button, "visible", this, "problem");
            button.addEventListener(MouseEvent.CLICK, onClick);
        }
        addChild(button);
    }

    private function onClick(event:MouseEvent):void {
        if (activity is DataSourceRefreshActivity) {
            var dsActivity:DataSourceRefreshActivity = activity as DataSourceRefreshActivity;
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DATA_SOURCE_ADMIN, {feedID: dsActivity.dataSourceID})))
        }
    }

    private var activity:ScheduledActivity;
    
    private var _problem:Boolean;


    [Bindable(event="problemChanged")]
    public function get problem():Boolean {
        return _problem;
    }

    public function set problem(value:Boolean):void {
        if (_problem == value) return;
        _problem = value;
        dispatchEvent(new Event("problemChanged"));
    }

    override public function set data(val:Object):void {
        this.activity = val as ScheduledActivity;
        problem = activity.problemLevel > 0;
    }

    override public function get data():Object {
        return this.activity;
    }
}
}
