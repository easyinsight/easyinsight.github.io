/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/8/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.account {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.users.DataSourceStats")]
public class DataSourceStats {

    public var name:String;
    public var visible:Boolean;
    public var dataSourceID:int;
    public var size:int;
    public var childStats:ArrayCollection;

    public function DataSourceStats() {
    }
}
}
