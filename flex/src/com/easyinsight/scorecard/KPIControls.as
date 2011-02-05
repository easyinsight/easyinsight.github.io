package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIEvent;
import com.easyinsight.kpi.KPIParentWindow;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class KPIControls extends HBox {

    private var kpi:KPI;
    private var editButton:Button;
    private var deleteButton:Button;
    private var copyButton:Button;

    private var _scorecardID:int;

    public function set scorecardID(value:int):void {
        _scorecardID = value;
    }

    [Bindable]
    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/copy.png")]
    private var copyIcon:Class;

    private var kpiService:RemoteObject;

    private var _groupID:int;

    public function KPIControls() {
        super();
        editButton = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        addChild(editButton);
        copyButton = new Button();
        copyButton.setStyle("icon", copyIcon);
        copyButton.addEventListener(MouseEvent.CLICK, onCopy);
        addChild(copyButton);
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        addChild(deleteButton);
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    public function set groupID(value:int):void {
        _groupID = value;
    }

    private function onCopy(event:MouseEvent):void {
        kpiService = new RemoteObject();
        kpiService.destination = "kpiService";
        kpiService.copyKPI.addEventListener(ResultEvent.RESULT, onCopyResult);
        kpiService.copyKPI.send(kpi);
    }

    private function onCopyResult(event:ResultEvent):void {
        var copy:KPI = kpiService.copyKPI.lastResult as KPI;
        var kpiWindow:KPIParentWindow = new KPIParentWindow();
        kpiWindow.scorecardID = _scorecardID;
        kpiWindow.kpi = copy;
        kpiWindow.addEventListener(KPIEvent.KPI_EDITED, copiedKPI, false, 0, true);
        PopUpManager.addPopUp(kpiWindow, this, true);
        PopUpUtil.centerPopUp(kpiWindow);
    }

    private function copiedKPI(event:KPIEvent):void {
        dispatchEvent(new KPIEvent(KPIEvent.KPI_ADDED, event.kpi));
    }

    private function onEdit(event:MouseEvent):void {
        var kpiWindow:KPIParentWindow = new KPIParentWindow();
        kpiWindow.scorecardID = _scorecardID;
        kpiWindow.kpi = kpi;
        kpiWindow.groupID = _groupID;
        kpiWindow.addEventListener(KPIEvent.KPI_EDITED, updatedKPI, false, 0, true);
        PopUpManager.addPopUp(kpiWindow, this, true);
        PopUpUtil.centerPopUp(kpiWindow);
    }

    private function updatedKPI(event:KPIEvent):void {
        dispatchEvent(event);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new KPIEvent(KPIEvent.KPI_REMOVED, kpi));
        /*kpiService = new RemoteObject();
        kpiService.destination = "scorecardService";
        kpiService.removeKPIFromScorecard.addEventListener(ResultEvent.RESULT, onDeleteResult);
        kpiService.removeKPIFromScorecard.send(kpi.kpiID, _scorecardID);*/
    }

    override public function set data(val:Object):void {
        kpi = val as KPI;
    }

    override public function get data():Object {
        return kpi;
    }
}
}