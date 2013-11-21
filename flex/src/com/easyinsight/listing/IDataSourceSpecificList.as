/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/5/12
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.solutions.DataSourceDescriptor;

import flash.events.Event;

import mx.collections.ArrayCollection;

public interface IDataSourceSpecificList {
    function get dataSourceDescriptor():DataSourceDescriptor;
    function set dataSourceDescriptor(value:DataSourceDescriptor):void;
    function set showBack(back:Boolean):void;
    function updateUI(event:Event = null):void;
    function set tags(values:ArrayCollection):void;
}
}
