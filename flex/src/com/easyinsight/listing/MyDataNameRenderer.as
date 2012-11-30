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
import flash.text.TextFormat;

import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.core.UITextFormat;

public class MyDataNameRenderer extends UITextField implements IListItemRenderer {
    public function MyDataNameRenderer() {
        addEventListener(MouseEvent.ROLL_OVER, onRollover);
        addEventListener(MouseEvent.ROLL_OUT, onRollout);
        addEventListener(MouseEvent.CLICK, onClick);
        /*useHandCursor = true;
        buttonMode = true;
        mouseChildren = false;*/
    }

    public function validateProperties():void {
        validateNow();
    }

    public function validateSize(recursive:Boolean = false):void {
        validateNow();
    }

    public function validateDisplayList():void {
        validateNow();
    }

    private function onRollover(event:MouseEvent):void {
        var tf:TextFormat = getTextFormat();
        tf.underline = true;
        setTextFormat(tf);
    }

    private function onRollout(event:MouseEvent):void {
        var tf:TextFormat = getTextFormat();
        tf.underline = false;
        setTextFormat(tf);
    }

    private var dataSource:DataSourceDescriptor;

    public function set data(val:Object):void {
        dataSource = val as DataSourceDescriptor;
        this.text = dataSource.name;
    }

    public function get data():Object {
        return dataSource;
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new DataSourceEvent(DataSourceEvent.NAVIGATE_TO_DATA_SOURCE, dataSource));
    }
}
}
