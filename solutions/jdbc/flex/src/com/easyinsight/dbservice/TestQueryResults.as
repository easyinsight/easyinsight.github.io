package com.easyinsight.dbservice {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.dbservice.TestQueryResults")]
public class TestQueryResults {
    public var results:ArrayCollection;
    
    public function TestQueryResults() {
    }
}
}