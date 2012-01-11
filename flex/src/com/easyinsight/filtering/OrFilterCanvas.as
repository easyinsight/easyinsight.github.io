/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/17/11
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.skin.ImageConstants;

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

    private function onAdd(event:MouseEvent):void {
        orContainer.addNewFilter(false);
    }
    
    [Embed(source="../../../../assets/add.png")]
    private var addIcon:Class;

    override protected function createChildren():void {
        super.createChildren();
        addChild(orContainer);
        if (_filterEditable) {
            var addButton:Button = new Button();
            addButton.setStyle("icon", addIcon);
            addButton.addEventListener(MouseEvent.CLICK, onAdd);
            addChild(addButton);
            var deleteButton:Button = new Button();
            deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
            deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
            addChild(deleteButton);
        }
    }

    private var _filterEditable:Boolean = false;

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
        _filterEditable = editable;
        orContainer.filterEditable = editable;
    }

    public function set loadingFromReport(loading:Boolean):void {
        orContainer.loadingFromReport = loading;
    }
}
}
