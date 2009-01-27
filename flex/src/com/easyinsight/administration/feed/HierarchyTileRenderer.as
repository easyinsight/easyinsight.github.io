package com.easyinsight.administration.feed {
import com.easyinsight.analysis.NamedKey;
import mx.binding.utils.BindingUtils;
import mx.events.FlexEvent;
import mx.controls.Label;
import mx.controls.Image;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import mx.containers.VBox;
public class HierarchyTileRenderer extends VBox {

    [Bindable]
    [Embed(source="../../../../../assets/cubes_blue_add.png")]
    private var cubeAddImage:Class;
    private var iconImage:Image;
    private var nameLabel:Label;

    private var analysisHierarchyItem:AnalysisHierarchyItem;

    private var _hierarchyName:String;

    public function HierarchyTileRenderer() {
        nameLabel = new Label();
        setStyle("horizontalAlign", "center");
    }

    override protected function createChildren():void {
        super.createChildren();
        iconImage = new Image();
        iconImage.source = cubeAddImage;
        addChild(iconImage);
        addChild(nameLabel);
    }

    [Bindable]
    public function get hierarchyName():String {
        return _hierarchyName;
    }
    public function set hierarchyName(val:String):void {
        _hierarchyName = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }
    
    override public function set data(val:Object):void {
        analysisHierarchyItem = val as AnalysisHierarchyItem;
        var key:NamedKey = analysisHierarchyItem.key as NamedKey;
        BindingUtils.bindProperty(nameLabel, "text", key, "nameValue");
    }

    override public function get data():Object {
        return analysisHierarchyItem;
    }
}
}