package com.easyinsight.goals {
import com.easyinsight.framework.ModuleAnalyzeSource;
import com.easyinsight.genredata.ModuleAnalyzeEvent;
import com.easyinsight.listing.DataFeedDescriptor;
import com.easyinsight.listing.DescriptorAnalyzeSource;
import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.LinkButton;
public class DataSourceLinkButton extends HBox{

    private var linkButton:LinkButton;
    private var goalDataSource:GoalFeed;

    public function DataSourceLinkButton() {
        super();
        linkButton = new LinkButton();
        linkButton.setStyle("textDecoration", "underline");
        linkButton.setStyle("fontSize", 13);
        linkButton.addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        var descriptor:DataFeedDescriptor = new DataFeedDescriptor();
        descriptor.name = goalDataSource.feedName;
        descriptor.dataFeedID = goalDataSource.feedID;
        var analyzeSource:ModuleAnalyzeSource = new DescriptorAnalyzeSource(descriptor);
        dispatchEvent(new ModuleAnalyzeEvent(analyzeSource));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(linkButton);
    }

    override public function set data(val:Object):void {
        this.goalDataSource = val as GoalFeed;
        var name:String = goalDataSource.feedName;
        linkButton.label =  name;
    }

    override public function get data():Object {
        return goalDataSource;
    }
}
}