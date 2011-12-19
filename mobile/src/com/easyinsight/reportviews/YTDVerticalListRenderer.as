/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/4/11
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {

import spark.skins.spark.DefaultGridItemRenderer;

public class YTDVerticalListRenderer extends DefaultGridItemRenderer {

    public function YTDVerticalListRenderer() {
        setStyle("textAlign", "right");
    }

    private var _qualifiedName:String;

    public function set qualifiedName(value:String):void {
        _qualifiedName = value;
    }

    private var value:Object;

    override public function set data(value:Object):void {
        this.value = value;
        if (value != null) {
            var color:uint = value[_qualifiedName + "color"] as uint;
            setStyle("color", color);
        }
    }

    override public function get data():Object {
        return this.value;
    }
}
}
