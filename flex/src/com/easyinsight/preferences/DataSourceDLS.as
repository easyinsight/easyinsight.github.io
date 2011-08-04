/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.preferences {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.DataSourceDLS")]
public class DataSourceDLS {

    public var dataSourceID:int;
    public var dataSourceName:String;
    public var filters:ArrayCollection;
    public var dataSourceDLSID:int;

    public function DataSourceDLS() {
    }

    public function get display():String {
        return dataSourceName;
    }
}
}
