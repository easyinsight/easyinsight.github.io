package com.easyinsight.datasources {
import com.easyinsight.customupload.BaseCampDataSourceCreation;
import com.easyinsight.listing.DataFeedDescriptor;

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

    override public function isLiveData():Boolean {
        return false;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.BASECAMP;
    }

    override public function configClass():Class {
        return BaseCampDataSourceCreation;
    }
}
}