/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/22/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.JSONDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.json.JSONDataSource")]
public class JSONDataSource extends ServerDataSourceDefinition {

    public var url:String;

    public function JSONDataSource() {
    }

    override public function getFeedType():int {
        return DataSourceType.JSON;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return JSONDataSourceCreation;
    }
}
}
