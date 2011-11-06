/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/28/11
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.solutions.DataSourceDescriptor;

import flash.events.MouseEvent;

import mx.controls.Label;

public class MyDataNameRenderer extends Label {
    public function MyDataNameRenderer() {
        addEventListener(MouseEvent.ROLL_OVER, onRollover);
        addEventListener(MouseEvent.ROLL_OUT, onRollout);
        addEventListener(MouseEvent.CLICK, onClick);
        useHandCursor = true;
        buttonMode = true;
        mouseChildren = false;
    }

    private function onRollover(event:MouseEvent):void {
        setStyle("textDecoration", "underline");
    }

    private function onRollout(event:MouseEvent):void {
        setStyle("textDecoration", "none");
    }

    private var dataSource:DataSourceDescriptor;

    override public function set data(val:Object):void {
        dataSource = val as DataSourceDescriptor;
        this.text = dataSource.name;
    }

    override public function get data():Object {
        return dataSource;
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new DataSourceEvent(DataSourceEvent.NAVIGATE_TO_DATA_SOURCE, dataSource));
    }
}
}
