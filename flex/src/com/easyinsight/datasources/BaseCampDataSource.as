package com.easyinsight.datasources {
import com.easyinsight.customupload.BaseCampConfiguration;
import com.easyinsight.customupload.BaseCampDataSourceCreation;

import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.basecamp.BaseCampCompositeSource")]
public class BaseCampDataSource extends CompositeServerDataSource {

    public var url:String;
    public var includeArchived:Boolean;
    public var includeInactive:Boolean;
    public var includeMilestoneComments:Boolean;
    public var includeTodoComments:Boolean;
    public var token:String;

    public function BaseCampDataSource() {
        super();
        this.feedName = "BaseCamp";
    }

    override public function getFeedType():int {
        return DataSourceType.BASECAMP;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:BaseCampConfiguration = new BaseCampConfiguration();
        config.dataSourceDefinition = this;
        config.label = "Basecamp Server Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return BaseCampDataSourceCreation;
    }
}
}