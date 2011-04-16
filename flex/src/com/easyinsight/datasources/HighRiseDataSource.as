package com.easyinsight.datasources {
import com.easyinsight.customupload.HighRiseDataSourceCreation;


import com.easyinsight.customupload.HighriseConfiguration;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.highrise.HighRiseCompositeSource")]
public class HighRiseDataSource extends CompositeServerDataSource {

    public var url:String;
    public var includeEmails:Boolean;
    public var includeContactNotes:Boolean;
    public var includeCompanyNotes:Boolean;
    public var includeDealNotes:Boolean;
    public var includeCaseNotes:Boolean;
    public var joinDealsToContacts:Boolean;
    public var joinTasksToContacts:Boolean;
    public var token:String;
    public var additionalTokens:ArrayCollection;

    public function HighRiseDataSource() {
        super();
        this.feedName = "Highrise";
    }

    override public function getFeedType():int {
        return DataSourceType.HIGHRISE;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:HighriseConfiguration = new HighriseConfiguration();
        config.dataSourceDefinition = this;
        config.label = "Highrise Server Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return HighRiseDataSourceCreation;
    }
}
}