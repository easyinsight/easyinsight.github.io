/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/21/12
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.administration.feed.GoogleFeedDefinition;
import com.easyinsight.google.GoogleSpreadsheetResponse;
import com.easyinsight.google.GoogleWindow;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.events.Event;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

import mx.rpc.events.ResultEvent;

import mx.rpc.remoting.RemoteObject;

public class GoogleSpreadsheetSheetRetrieval extends EventDispatcher implements IPostOAuth {

    private var uploadService:RemoteObject;
    public var newSource:Boolean;

    public var dataSource:GoogleFeedDefinition;

    public var parent:UIComponent;

    public function GoogleSpreadsheetSheetRetrieval() {
        uploadService = new RemoteObject();
        uploadService.destination = "google";
        uploadService.getSpreadsheets.addEventListener(ResultEvent.RESULT, onAccounts);
    }

    private function onAccounts(event:ResultEvent):void {
        var resp:GoogleSpreadsheetResponse = uploadService.getSpreadsheets.lastResult as GoogleSpreadsheetResponse;
        var accounts:ArrayCollection = resp.spreadsheets;
        if (accounts.length == 0) {
            Alert.show("We were unable to find any Google spreadsheets for your authenticated Google user. Check that you created this connection under the right Google account. If this problem persists, contact support@easy-insight.com.");
            dispatchEvent(new Event(Event.CANCEL));
        } else {
            dispatchEvent(new Event(Event.CANCEL));
            var googleWindow:GoogleWindow = new GoogleWindow();
            googleWindow.newConnection = newSource;
            googleWindow.dataSource = dataSource;
            googleWindow.googleSpreadsheets = accounts;
            googleWindow.addEventListener(Event.COMPLETE, onComplete);
            PopUpManager.addPopUp(googleWindow, parent, true);
            PopUpUtil.centerPopUp(googleWindow);
        }
    }

    private function onComplete(event:Event):void {
        dispatchEvent(event);
    }

    public function retrieve(parent:UIComponent):void {
        this.parent = parent;
        ProgressAlert.alert(parent,  "Retrieving your Google spreadsheets...", null, uploadService.getSpreadsheets);
        uploadService.getSpreadsheets.send(dataSource);
    }
}
}
