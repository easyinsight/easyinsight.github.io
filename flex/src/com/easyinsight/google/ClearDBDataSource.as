/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/17/10
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.cleardb.ClearDBDataSource")]
public class ClearDBDataSource extends ServerDataSourceDefinition {

    public var tableName:String;

    public function ClearDBDataSource() {
        super();
    }

    override public function isLiveData():Boolean {
        return true;
    }
}
}
