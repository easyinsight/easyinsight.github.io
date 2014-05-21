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
[RemoteClass(alias="com.easyinsight.datafeeds.netsuite.NetsuiteQueryConnection")]
public class NetsuiteQueryConnection extends ServerDataSourceDefinition {

    public var query:String;
    public var timeout:int = 5;
    public var rebuildFields:Boolean;
    public var netsuiteUserName:String;
    public var netsuitePassword:String;
    public var accountID:String;

    public function NetsuiteQueryConnection() {
    }

    override public function getFeedType():int {
        return DataSourceType.NETSUITE;
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
