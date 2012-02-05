/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/8/11
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.DeliveryInfo")]
public class DeliveryInfo implements IDeliverable {

    public static const REPORT:int = 1;
    public static const SCORECARD:int = 2;

    public var name:String;
    public var filters:ArrayCollection;
    public var dataSourceID:int;
    public var id:int;
    public var index:int;
    public var type:int;
    public var format:int;
    public var label:String;
    public var sendIfNoData:Boolean = true;

    public function DeliveryInfo() {
    }

    public function setFormat(format:int):void {
        this.format = format;
    }

    public function setFilters(filters:ArrayCollection):void {
        this.filters = filters;
    }

    public function setName(name:String):void {
        this.name = name;
    }

    public function setLabel(label:String):void {
        this.label = label;
    }
    
    public function get display():String {
        if (label != null && label != "") {
            return label;
        }
        return name;
    }

    public function setSendOnNoData(noData:Boolean):void {
        this.sendIfNoData = noData;
    }
}
}
