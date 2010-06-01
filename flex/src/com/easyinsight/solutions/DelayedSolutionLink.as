package com.easyinsight.solutions {
import com.easyinsight.genredata.AnalyzeEvent;

import com.easyinsight.listing.IPerspective;
import com.easyinsight.listing.ListingChangeEvent;

import flash.events.EventDispatcher;

import mx.containers.Box;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedSolutionLink extends EventDispatcher{

    private var solutionID:int;
    private var solutionService:RemoteObject;
    private var auth:Boolean;
    private var properties:Object;

    public function DelayedSolutionLink(solutionID:int, auth:Boolean = false, properties:Object = null) {
        super();
        this.solutionID = solutionID;
        this.auth = auth;
        this.properties = properties;
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.retrieveSolution.addEventListener(ResultEvent.RESULT, gotSolution);
    }

    public function execute():void {
        solutionService.retrieveSolution.send(solutionID);
    }

    private function gotSolution(event:ResultEvent):void {
        var result:Solution = solutionService.retrieveSolution.lastResult as Solution;
        if (result != null) {
            //var solutionDetail:RevisedSolutionDetail = new RevisedSolutionDetail();
            var box:IPerspective = SolutionDetailFactory.createDetailPage(result, auth);
            if (properties != null) {
                for (var propKey:String in properties) {
                    box[propKey] = properties[propKey];
                }
            }
            dispatchEvent(new ListingChangeEvent(box));
            //dispatchEvent(new AnalyzeEvent(new SolutionAnalyzeSource(result)));
        }
    }
}
}