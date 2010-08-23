package com.easyinsight.etl {
import com.easyinsight.framework.PerspectiveInfo;

public class LookupTableSource extends PerspectiveInfo {

    public function LookupTableSource(lookupTable:int) {
        super(PerspectiveInfo.LOOKUP_TABLE, new Object());
        properties.lookupTableID = lookupTable;
    }
}
}