/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/17/10
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import com.easyinsight.customupload.QuickBaseConfiguration;
import com.easyinsight.customupload.QuickbaseDataSourceCreation;
import com.easyinsight.datasources.CompositeServerDataSource;
import com.easyinsight.datasources.DataSourceType;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.quickbase.QuickbaseCompositeSource")]
public class QuickbaseCompositeSource extends CompositeServerDataSource {

    public var qbUserName:String;
    public var qbPassword:String;

    public var supportIndex:Boolean;
    public var preserveCredentials:Boolean;

    public var applicationToken:String;
    public var applicationId:String;
    public var sessionTicket:String;
    public var host:String;

    public var inlineUsers:Boolean = false;

    public var rebuildFields:Boolean = false;

    public function QuickbaseCompositeSource() {
        super();
        this.feedName = "Quickbase";
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var index:FieldIndexing = new FieldIndexing();
        index.fields = fields;

        index.label = "Indexing";
        var config:QuickBaseConfiguration = new QuickBaseConfiguration();

        config.label = "QuickBase Server Configuration";
        pages.addItem(config);
        pages.addItem(index);
        return pages;
    }

    override public function getFeedType():int {
        return DataSourceType.QUICKBASE;
    }

    override public function configClass():Class {
        return QuickbaseDataSourceCreation;
    }
}
}
