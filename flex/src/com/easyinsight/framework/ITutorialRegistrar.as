package com.easyinsight.framework {
import com.easyinsight.tour.TutorialFramework;

public interface ITutorialRegistrar {
    function registerTutorialForApp(tutorial:TutorialFramework):void;
}
}