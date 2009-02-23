package com.easyinsight.goals {
import flash.display.DisplayObject;
import mx.modules.Module;
public class MyGoalsModule extends Module implements IGoalsModule {

    public function MyGoalsModule() {
        super();
    }


    public function createDisplayObject():DisplayObject {
        var goalGrid:MyGoalsGrid = new MyGoalsGrid();
        goalGrid.percentHeight = 100;
        goalGrid.percentWidth = 100;
        goalGrid.setStyle("backgroundAlpha", .5);
        return goalGrid;
    }
}
}