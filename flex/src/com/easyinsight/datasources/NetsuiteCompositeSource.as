/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.NetsuiteDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.netsuite.NetsuiteTableSource")]
public class NetsuiteCompositeSource extends ServerDataSourceDefinition {

    public var table:String;
    public var searchID:String;
    public var nsUsername:String;
    public var nsPassword:String;
    public var accountID:String;
    public var netsuiteRole:String;

    public function NetsuiteCompositeSource() {
    }

    override public function getFeedType():int {
        return DataSourceType.NETSUITE_COMPOSITE;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return NetsuiteDataSourceCreation;
    }

    override public function allowFieldEdit():Boolean {
        return true;
    }
}
}
