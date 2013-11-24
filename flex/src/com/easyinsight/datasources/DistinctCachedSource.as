/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/18/13
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.DistinctCachedDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.DistinctCachedSource")]
public class DistinctCachedSource extends ServerDataSourceDefinition {

    public var reportID:int;

    public function DistinctCachedSource() {
    }

    override public function getFeedType():int {
        return DataSourceType.DISTINCT_CACHED;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return DistinctCachedDataSourceCreation;
    }
}
}
