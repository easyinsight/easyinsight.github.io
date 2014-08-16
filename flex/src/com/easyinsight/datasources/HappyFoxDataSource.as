package com.easyinsight.datasources {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.happyfox.HappyFoxCompositeSource")]
public class HappyFoxDataSource extends CompositeServerDataSource {

    public var url:String;
    public var hfApiKey:String;
    public var authKey:String;

    public function HappyFoxDataSource() {
        super();
        this.feedName = "Happy Fox";
    }

    override public function createAdminPages():ArrayCollection {
            var pages:ArrayCollection = new ArrayCollection();
            return pages;
        }


    override public function getFeedType():int {
        return DataSourceType.HAPPY_FOX;
    }

    override public function configClass():Class {
        return HappyFoxDataSourceCreation;
    }


}
}