package com.easyinsight.framework {
import com.easyinsight.listing.IPerspective;

import flash.display.DisplayObject;

public class ModulePage {
    public function ModulePage() {
    }

    public function createScreen():FrontScreen {
        var frontScreen:FrontScreen = new FrontScreen();
        frontScreen.addEventListener(ModulePageEvent.MODULE_PAGE_LOADED, onLoaded);
        frontScreen.moduleName = getModuleName();
        return frontScreen;
    }

    private function onLoaded(event:ModulePageEvent):void {
        event.currentTarget.removeEventListener(ModulePageEvent.MODULE_PAGE_LOADED, onLoaded);
        configure(event.perspective);
        event.screen.addChild(event.perspective as DisplayObject);
        event.perspective.gotFocus();
    }

    protected function getModuleName():String {
        return null;
    }

    protected function configure(perspective:IPerspective):void {

    }
}
}