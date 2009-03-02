package com.easyinsight.goals {
import com.easyinsight.framework.User;
import flash.events.Event;
import flash.events.HTTPStatusEvent;
import flash.events.IOErrorEvent;
import flash.events.MouseEvent;
import flash.events.ProgressEvent;
import flash.events.SecurityErrorEvent;
import flash.net.FileReference;
import flash.net.URLRequest;
import flash.net.URLRequestMethod;
import flash.net.URLVariables;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
public class AssociatedSolutionsAdminControls extends HBox{

    private var goalSolution:GoalSolution;

    [Bindable]
    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/gear.png")]
    private var archiveIcon:Class;

    private var fileButton:Button;
    private var deleteButton:Button;

    private var fileRef:FileReference;

    public function AssociatedSolutionsAdminControls() {
        super();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
        fileButton = new Button();
        fileButton.setStyle("icon", archiveIcon);
        fileButton.toolTip = "Download Associated Files...";
        fileButton.addEventListener(MouseEvent.CLICK, downloadFiles);
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.toolTip = "Delete Association";
        deleteButton.addEventListener(MouseEvent.CLICK, deleteAssociation);
    }

    private function downloadFiles():void {
        var request:URLRequest = new URLRequest("/app/DownloadServlet");
        request.method = URLRequestMethod.GET;
        var vars:URLVariables = new URLVariables();

        vars.userName = new String(User.getInstance().userName);
        vars.password = new String(User.getInstance().password);
        vars.operation = new String(2);
        vars.fileID = new String(goalSolution.solutionID);
        request.data = vars;

        fileRef = new FileReference();
        fileRef.addEventListener(Event.CANCEL, doEvent);
        fileRef.addEventListener(Event.COMPLETE, complete);
        fileRef.addEventListener(Event.OPEN, doEvent);
        fileRef.addEventListener(Event.SELECT, doEvent);
        fileRef.addEventListener(HTTPStatusEvent.HTTP_STATUS, doEvent);
        fileRef.addEventListener(IOErrorEvent.IO_ERROR, doEvent);
        fileRef.addEventListener(ProgressEvent.PROGRESS, doEvent);
        fileRef.addEventListener(SecurityErrorEvent.SECURITY_ERROR, doEvent);

        fileRef.download(request, goalSolution.solutionArchiveName);
    }

    private function doEvent(event:Event):void {
        trace(event);
    }

    private function complete(event:Event):void {
        Alert.show("Solution files copied!");
    }

    private function deleteAssociation():void {
        dispatchEvent(new DeleteAssociationEvent(DeleteAssociationEvent.DELETE_SOLUTION, goalSolution));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(fileButton);
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        goalSolution = val as GoalSolution;
        fileButton.visible = goalSolution.solutionArchiveName != null;
    }

    override public function get data():Object {
        return goalSolution;
    }
}
}