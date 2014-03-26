package com.easyinsight.datasources {
import com.easyinsight.customupload.LinkedInCompositeDataSourceCreation;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.linkedin.LinkedInCompositeSource")]
public class LinkedinCompositeDataSource extends CompositeServerDataSource {

    public var tokenKey:String;
    public var tokenSecret:String;

    public function LinkedinCompositeDataSource() {
        super();
        feedName = "Linkedin";
    }

    override public function getFeedType():int {
        return DataSourceType.LINKEDIN_COMPOSITE;
    }

    override public function configClass():Class {
        return LinkedInCompositeDataSourceCreation;
    }
}
}