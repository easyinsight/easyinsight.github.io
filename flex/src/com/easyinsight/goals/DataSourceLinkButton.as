package com.easyinsight.goals {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.listing.AnalysisDefinitionAnalyzeSource;
import com.easyinsight.listing.AnalyzeSource;
import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.LinkButton;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;
public class DataSourceLinkButton extends HBox{

    private var linkButton:LinkButton;
    private var goalDataSource:GoalFeed;
    private var analysisService:RemoteObject;

    public function DataSourceLinkButton() {
        super();
        linkButton = new LinkButton();
        linkButton.setStyle("textDecoration", "underline");
        linkButton.setStyle("fontSize", 13);
        linkButton.addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        analysisService = new RemoteObject();
        analysisService.destination = "analysisDefinition";
        analysisService.openAnalysisDefinition.addEventListener(ResultEvent.RESULT, gotReport);
    }

    private function gotReport(event:ResultEvent):void {
        var def:AnalysisDefinition = analysisService.openAnalysisDefinition.lastResult as AnalysisDefinition;
        var analyzeSource:AnalyzeSource = new AnalysisDefinitionAnalyzeSource(def);
        dispatchEvent(new AnalyzeEvent(analyzeSource));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(linkButton);
    }

    override public function set data(val:Object):void {
        this.goalDataSource = val as GoalFeed;
        linkButton.label = goalDataSource.feedName;
    }

    override public function get data():Object {
        return goalDataSource;
    }
}
}