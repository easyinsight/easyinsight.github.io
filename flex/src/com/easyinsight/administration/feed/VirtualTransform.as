package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisDimension;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.VirtualTransform")]
public class VirtualTransform {

    public var virtualTransformID:int;
    public var transformDimension:AnalysisDimension;
    public var assignedValues:ArrayCollection = new ArrayCollection();
    public var stringValues:ArrayCollection = new ArrayCollection();

    private var editor:VirtualTransformEditor;

    public function VirtualTransform() {
    }

    public function setEditor(editor:VirtualTransformEditor):void {
        this.editor = editor;
    }

    public function getEditor():VirtualTransformEditor {
        return this.editor;
    }

    private function blah():void {
        var value:PersistableValue;
        var stringValue:PersistableStringValue;
    }
}
}