/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/26/11
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.solutions {
import com.easyinsight.quicksearch.EIDescriptor;

import flash.utils.ByteArray;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.core.DataSourceDescriptor")]
public class DataSourceDescriptor extends EIDescriptor{

    public var description:String;
    public var dataSourceBehavior:int;
    public var dataSourceType:int;
    public var size:int;
    public var lastDataTime:Date;
    public var groupSourceID:int;
    public var logoImage:ByteArray;
    public var children:ArrayCollection;
    public var customFolders:ArrayCollection;

    public function DataSourceDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.DATA_SOURCE;
    }
}
}