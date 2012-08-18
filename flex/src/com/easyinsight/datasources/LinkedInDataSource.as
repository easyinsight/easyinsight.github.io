package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.LinkedInDataSourceCreation;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.linkedin.LinkedInDataSource")]
public class LinkedInDataSource extends ServerDataSourceDefinition {

    public var pin:String;
    public var tokenKey:String;
    public var tokenSecret:String;

    public function LinkedInDataSource() {
        super();
        feedName = "LinkedIn";
    }

    override public function getFeedType():int {
        return DataSourceType.LINKEDIN;
    }

    override public function configClass():Class {
        return LinkedInDataSourceCreation;
    }
}
}