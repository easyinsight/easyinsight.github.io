package com.easyinsight.customupload.api {
import mx.collections.ArrayCollection;
import mx.core.IFactory;
public class ParameterChoiceBoxFactory implements IFactory {

    public var analysisItems:ArrayCollection;

    public function newInstance():* {
        return new ParameterChoiceBox(analysisItems);
    }
}
}