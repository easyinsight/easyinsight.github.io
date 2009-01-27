package com.easyinsight.customupload.api {
import flash.events.Event;
public class MethodDefinitionEvent extends Event {

    public static const METHOD_DEFINED:String = "methodDefined";
    public static const METHOD_REMOVED:String = "methodRemoved";
    public static const METHOD_EDITED:String = "methodEdited";

    public var method:ConfiguredMethod;

    public function MethodDefinitionEvent(type:String, method:ConfiguredMethod) {
        super(type, true);
        this.method = method;
    }


    override public function clone():Event {
        return new MethodDefinitionEvent(type, method);
    }
}
}