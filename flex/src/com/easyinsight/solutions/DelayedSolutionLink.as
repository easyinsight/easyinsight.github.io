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

    public function DelayedSolutionLink(solutionID:int, auth:Boolean = false) {
        super();
        this.solutionID = solutionID;
        this.auth = auth;
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
            dispatchEvent(new ListingChangeEvent(box));
            //dispatchEvent(new AnalyzeEvent(new SolutionAnalyzeSource(result)));
        }
    }
}
}