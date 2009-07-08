package com.easyinsight.dashboard {
import com.easyinsight.quicksearch.EIDescriptor;

public class AirIntroDescriptor extends EIDescriptor{
    public function AirIntroDescriptor() {
        super();
    }


    override public function getType():int {
        return EIDescriptor.AIR_INTRO;
    }
}
}