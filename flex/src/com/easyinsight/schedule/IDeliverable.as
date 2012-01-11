/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/9/12
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import mx.collections.ArrayCollection;

public interface IDeliverable {
    function setFormat(format:int):void;
    function setFilters(filters:ArrayCollection):void;
    function setName(name:String):void;
    function setLabel(label:String):void;
}
}
