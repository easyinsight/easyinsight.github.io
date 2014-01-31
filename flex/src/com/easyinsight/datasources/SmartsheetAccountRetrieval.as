/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/21/12
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
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

public class SmartsheetAccountRetrieval extends EventDispatcher implements IPostOAuth {

    private var uploadService:RemoteObject;

    public var dataSource:SmartsheetTableSource;

    public var parent:UIComponent;

    public function SmartsheetAccountRetrieval() {
        uploadService = new RemoteObject();
        uploadService.destination = "userUpload";
        uploadService.getSmartsheetTables.addEventListener(ResultEvent.RESULT, onAccounts);
    }

    private function onAccounts(event:ResultEvent):void {
        var accounts:ArrayCollection = uploadService.getSmartsheetTables.lastResult as ArrayCollection;
        if (accounts.length == 0) {
            Alert.show("We were unable to find any Smartsheet sheets for your authenticated Smartsheet user.");
            dispatchEvent(new Event(Event.CANCEL));
        } else if (accounts.length == 1) {
            // accounts
            var account:BasecampNextAccount = accounts.getItemAt(0) as BasecampNextAccount;
            dataSource.table = account.id;
            dataSource.feedName = account.name;
            dispatchEvent(new Event(Event.COMPLETE));
        } else {
            var picker:SmartsheetTablePicker = new SmartsheetTablePicker();
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
        ProgressAlert.alert(parent,  "Retrieving your Smartsheet sheets...", null, uploadService.getSmartsheetTables);
        uploadService.getSmartsheetTables.send(dataSource);
    }
}
}
