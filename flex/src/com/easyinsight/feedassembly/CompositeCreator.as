/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/9/11
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.listing.CompositeFeedCreationSource;

import com.easyinsight.util.PopUpUtil;

import com.easyinsight.util.ProgressAlert;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class CompositeCreator extends EventDispatcher {

    private var feedService:RemoteObject;

    public var dataSources:ArrayCollection = new ArrayCollection();

    private var parent:UIComponent;

    public function CompositeCreator(parent:UIComponent) {
        this.parent = parent;
    }

    public function start():void {
        var window:JoinChoiceWindow = new JoinChoiceWindow();
        window.addEventListener("joinSources", onJoin);
        window.addEventListener("federateSources", onFederate);
        PopUpManager.addPopUp(window, parent, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onJoin(event:Event):void {
        feedService = new RemoteObject();
        feedService.destination = "feeds";
        feedService.suggestJoins.addEventListener(ResultEvent.RESULT, onJoins);
        ProgressAlert.alert(parent, "Analyzing...", null, feedService.suggestJoins);
        feedService.suggestJoins.send(dataSources);
    }

    private function onFederate(event:Event):void {
        dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.FEDERATED_EDITOR)));
    }

    private function onJoins(event:ResultEvent):void {
        var suggestions:ArrayCollection = feedService.suggestJoins.lastResult as ArrayCollection;
        if (suggestions.length == 0) {
            dispatchEvent(new AnalyzeEvent(new CompositeFeedCreationSource(dataSources)));
        } else {
            var window:SuggestedJoinWindow = new SuggestedJoinWindow();
            window.suggestions = suggestions;
            window.addEventListener(AnalyzeEvent.ANALYZE, onAnalyze);
            PopUpManager.addPopUp(window, parent, true);
            PopUpUtil.centerPopUp(window);
        }
    }

    private function onAnalyze(event:Event):void {
        dispatchEvent(event);
    }
}
}
