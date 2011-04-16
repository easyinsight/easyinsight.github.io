/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/17/10
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import com.easyinsight.datasources.ClearDBDataSourceCreation;
import com.easyinsight.datasources.CompositeServerDataSource;
import com.easyinsight.datasources.DataSourceType;

import com.easyinsight.feedassembly.CompositeWorkspace;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.cleardb.ClearDBCompositeSource")]
public class ClearDBCompositeSource extends CompositeServerDataSource {

    public var cdApiKey:String;
    public var appToken:String;

    public function ClearDBCompositeSource() {
        super();
        this.feedName = "Quickbase";
    }

    override public function getFeedType():int {
        return DataSourceType.CLEARDB;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var compositeWorkspace:CompositeWorkspace = new CompositeWorkspace();
        compositeWorkspace.addExistingDef(compositeFeedNodes, connections, dataFeedID);
        compositeWorkspace.label = "Composite Details";
        pages.addItem(compositeWorkspace);
        return pages;
    }

    override public function configClass():Class {
        return ClearDBDataSourceCreation;
    }
}
}
