package com.easyinsight.datasources {
import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.teamwork.TeamworkCompositeSource")]
public class TeamworkCompositeSource extends CompositeServerDataSource {

    public var url:String;
    public var teamworkApiKey:String;


    public function TeamworkCompositeSource() {
        super();
        this.feedName = "Teamwork";
    }

    override public function createAdminPages():ArrayCollection {
            var pages:ArrayCollection = new ArrayCollection();


            return pages;
        }


    override public function getFeedType():int {
        return DataSourceType.TEAMWORK;
    }

    override public function configClass():Class {
        return TeamworkDataSourceCreation;
    }

    override public function hasCustomFields():Boolean {
        return true;
    }
}
}