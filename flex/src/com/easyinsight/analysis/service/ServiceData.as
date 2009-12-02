package com.easyinsight.analysis.service {
import mx.collections.ArrayCollection;

public class ServiceData {

    public var data:ArrayCollection;
    public var clientProcessorMap:Object;


    public function ServiceData(data:ArrayCollection, clientProcessorMap:Object) {
        this.data = data;
        this.clientProcessorMap = clientProcessorMap;
    }
}
}