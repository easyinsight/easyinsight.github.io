/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/14/11
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
import com.easyinsight.administration.feed.FeedDefinitionData;

import com.easyinsight.analysis.FeedResponse;
import com.easyinsight.customupload.DelayedSync;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.solutions.DataSourceDescriptor;
import com.easyinsight.solutions.PostInstallSource;

import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.display.DisplayObject;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.core.Application;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class ConnectionConfig extends EventDispatcher {

    private var dataSourceKey:String;

    private var feedService:RemoteObject;

    public function ConnectionConfig(dataSourceKey:String) {
        this.dataSourceKey = dataSourceKey;
        feedService = new RemoteObject();
        feedService.destination = "feeds";
        feedService.getFeedDefinition.addEventListener(ResultEvent.RESULT, gotDataSource);
        feedService.openFeedIfPossible.addEventListener(ResultEvent.RESULT, onResult);
    }

    public function execute():void {
        ProgressAlert.alert(DisplayObject(Application.application), "Retrieving information...", null, feedService.openFeedIfPossible,
                feedService.getFeedDefinition);
        feedService.openFeedIfPossible.send(dataSourceKey);
    }

    private function onResult(event:ResultEvent):void {
        var response:FeedResponse = feedService.openFeedIfPossible.lastResult as FeedResponse;
        feedService.getFeedDefinition.send(response.feedDescriptor.id);
    }

    private function gotDataSource(event:ResultEvent):void {
        var dataSource:FeedDefinitionData = feedService.getFeedDefinition.lastResult as FeedDefinitionData;
        if (dataSource != null) {
            if (dataSource.isLiveData()) {
                var desc:DataSourceDescriptor = new DataSourceDescriptor();
                desc.name = dataSource.feedName;
                desc.id = dataSource.dataFeedID;
                dispatchEvent(new AnalyzeEvent(new PostInstallSource(desc)));
            } else {
                var delayedSync:DelayedSync = new DelayedSync();
                delayedSync.dataSourceDefinition = dataSource;
                delayedSync.addEventListener(AnalyzeEvent.ANALYZE, passThrough, false, 0, true);
                PopUpManager.addPopUp(delayedSync, DisplayObject(Application.application), true);
                PopUpUtil.centerPopUp(delayedSync);
            }
        }
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }
}
}
