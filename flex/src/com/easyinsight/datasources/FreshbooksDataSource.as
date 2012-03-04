package com.easyinsight.datasources {
import com.easyinsight.customupload.FreshBooksConfiguration;
import com.easyinsight.customupload.FreshBooksConfiguration;
import com.easyinsight.customupload.FreshbooksDataSourceCreation;

import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.freshbooks.FreshbooksCompositeSource")]
public class FreshbooksDataSource extends CompositeServerDataSource {

    public var url:String;
    public var pin:String;
    public var tokenKey:String;
    public var tokenSecretKey:String;
    public var liveDataSource:Boolean;

    public function FreshbooksDataSource() {
        super();
        feedName = "Freshbooks";
    }

    override public function getFeedType():int {
        return DataSourceType.FRESHBOOKS;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:FreshBooksConfiguration = new FreshBooksConfiguration();
        config.dataSourceDefinition = this;
        config.label = "FreshBooks Server Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return FreshbooksDataSourceCreation;
    }
}
}