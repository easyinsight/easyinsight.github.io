/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.DatabaseConnection")]
public class DatabaseConnection extends ServerDataSourceDefinition {

    public var refreshKey:String;
    public var refreshUrl:String;

    public function DatabaseConnection() {
    }

    override public function getFeedType():int {
        return DataSourceType.DATABASE;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }
}
}
