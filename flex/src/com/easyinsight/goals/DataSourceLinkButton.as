package com.easyinsight.goals {


import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.listing.AnalyzeSource;
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
        var analyzeSource:AnalyzeSource = new DescriptorAnalyzeSource(goalDataSource.feedID, goalDataSource.feedName);
        dispatchEvent(new AnalyzeEvent(analyzeSource));
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