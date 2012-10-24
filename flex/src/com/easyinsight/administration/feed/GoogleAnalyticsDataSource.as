package com.easyinsight.administration.feed {
import com.easyinsight.customupload.GoogleAnalyticsConfiguration;
import com.easyinsight.customupload.GoogleAnalyticsSourceCreation;
import com.easyinsight.datasources.DataSourceType;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.ganalytics.GoogleAnalyticsDataSource")]
public class GoogleAnalyticsDataSource extends ServerDataSourceDefinition{

    public var pin:String;
    public var tokenKey:String;
    public var tokenSecret:String;

    public function GoogleAnalyticsDataSource() {
        super();
        this.feedName = "Google Analytics";
    }

    override public function configClass():Class {
        return GoogleAnalyticsSourceCreation;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:GoogleAnalyticsConfiguration = new GoogleAnalyticsConfiguration();
        config.dataSourceDefinition = this;
        config.label = "Google Analytics Server Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function getFeedType():int {
        return DataSourceType.GOOGLE_ANALYTICS;
    }

    /*override public function isLiveData():Boolean {
        return true;
    }*/
}
}