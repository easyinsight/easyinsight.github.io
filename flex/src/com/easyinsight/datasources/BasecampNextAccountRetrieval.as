/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/21/12
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;
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

public class BasecampNextAccountRetrieval extends EventDispatcher implements IPostOAuth {

    private var uploadService:RemoteObject;

    public var dataSource:BasecampNextCompositeSource;

    public var parent:UIComponent;

    public function BasecampNextAccountRetrieval() {
        uploadService = new RemoteObject();
        uploadService.destination = "userUpload";
        uploadService.getBasecampAccounts.addEventListener(ResultEvent.RESULT, onAccounts);
    }

    private function onAccounts(event:ResultEvent):void {
        var accounts:ArrayCollection = uploadService.getBasecampAccounts.lastResult as ArrayCollection;
        if (accounts.length == 0) {
            Alert.show("We were unable to find a Basecamp account for your authenticated 37Signals user. Check that you created this connection under the right 37Signals user, and if so, that your Basecamp account is still active. If this problem persists, contact support@easy-insight.com.");
            dispatchEvent(new Event(Event.CANCEL));
        } else if (accounts.length == 1) {
            // accounts
            var account:BasecampNextAccount = accounts.getItemAt(0) as BasecampNextAccount;
            dataSource.endpoint = account.id;
            dispatchEvent(new Event(Event.COMPLETE));
        } else {
            var picker:BasecampNextAccountPicker = new BasecampNextAccountPicker();
            picker.dataSource = dataSource;
            picker.addEventListener(Event.COMPLETE, onComplete);
            PopUpManager.addPopUp(picker, parent, true);
            PopUpUtil.centerPopUp(picker);
        }
    }

    private function onComplete(event:Event):void {
        dispatchEvent(event);
    }

    public function retrieve(parent:UIComponent):void {
        this.parent = parent;
        ProgressAlert.alert(parent,  "Retrieving your Basecamp accounts...", null, uploadService.getBasecampAccounts);
        uploadService.getBasecampAccounts.send(dataSource);
    }
}
}
