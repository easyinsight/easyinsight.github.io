/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/17/11
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;

public class OrFilterCanvas extends HBox implements IFilter {

    private var orFilter:OrFilter;
    private var orContainer:OrTransformContainer;

    public function OrFilterCanvas(feedID:int) {
        orContainer = new OrTransformContainer();
        orContainer.feedID = feedID;
        orContainer.addEventListener(TransformsUpdatedEvent.UPDATED_TRANSFORMS, updatedTransforms);
        orContainer.parentFilter = this;
        setStyle("paddingLeft", 5);
        setStyle("paddingRight", 5);
        setStyle("paddingTop", 5);
        setStyle("paddingBottom", 5);
        setStyle("verticalAlign", "middle");
    }

    private function updatedTransforms(event:TransformsUpdatedEvent):void {
        dispatchEvent(event);
    }

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    override protected function createChildren():void {
        super.createChildren();
        addChild(orContainer);
        var deleteButton:Button = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        addChild(deleteButton);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new FilterDeletionEvent(this));
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void {
        this.orFilter = filterDefinition as OrFilter;
        orContainer.orFilter = orFilter;
    }

    public function get filterDefinition():FilterDefinition {
        return orFilter;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        orContainer.analysisItems = analysisItems;
    }

    public function set filterEditable(editable:Boolean):void {
        orContainer.filterEditable = editable;
    }

    public function set loadingFromReport(loading:Boolean):void {
        orContainer.loadingFromReport = loading;
    }
}
}
