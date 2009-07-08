package com.easyinsight.dashboard {
import com.easyinsight.quicksearch.EIDescriptor;

public class MyGoalsDescriptor extends EIDescriptor {
    public function MyGoalsDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.MY_GOALS;
    }
}
}