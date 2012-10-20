/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/22/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.JSONConfiguration;
import com.easyinsight.customupload.JSONDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.treasuredata.TreasureDataQuerySource")]
public class TreasureDataSource extends ServerDataSourceDefinition {

    public var databaseID:String;
    public var tdApiKey:String;
    public var query:String;

    public function TreasureDataSource() {
    }

    override public function getFeedType():int {
        return DataSourceType.TREASURE_DATA;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return JSONDataSourceCreation;
    }

    override public function allowFieldEdit():Boolean {
        return true;
    }
}
}
