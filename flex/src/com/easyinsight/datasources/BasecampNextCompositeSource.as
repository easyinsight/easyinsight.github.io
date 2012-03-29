package com.easyinsight.datasources {
import com.easyinsight.customupload.BasecampNextDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.basecampnext.BasecampNextCompositeSource")]
public class BasecampNextCompositeSource extends CompositeServerDataSource {

    public var endpoint:String;
    public var accessToken:String;
    public var refreshToken:String;

    public function BasecampNextCompositeSource() {
        super();
        this.feedName = "BaseCamp";
    }

    override public function getFeedType():int {
        return DataSourceType.BASECAMP_NEXT;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return BasecampNextDataSourceCreation;
    }
}
}