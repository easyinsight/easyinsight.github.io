package com.easyinsight.datasources {
import com.easyinsight.customupload.ConstantContactDataSourceCreation;
import com.easyinsight.customupload.TrelloDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.trello.TrelloCompositeSource")]
public class TrelloCompositeSource extends CompositeServerDataSource {

    public var pin:String;
    public var tokenKey:String;
    public var tokenSecret:String;

    public function TrelloCompositeSource() {
        super();
        feedName = "Trello";
    }

    override public function getFeedType():int {
        return DataSourceType.TRELLO;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return TrelloDataSourceCreation;
    }
}
}