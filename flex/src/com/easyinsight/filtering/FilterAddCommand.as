package com.easyinsight.filtering {
import com.easyinsight.commands.ICommand;
public class FilterAddCommand implements ICommand {
    private var transformContainer:TransformContainer;
    private var filter:IFilter;
    private var launchWindow:Boolean;
    private var includeCallout:Boolean;

    public function FilterAddCommand(transformContainer:TransformContainer, filter:IFilter, launchWindow:Boolean,
            includeCallout:Boolean = false) {
        this.transformContainer = transformContainer;
        this.filter = filter;
        this.launchWindow = launchWindow;
        this.includeCallout = includeCallout;
    }

    public function execute():void {
        transformContainer.commandFilterAdd2(filter, launchWindow, includeCallout);
    }

    public function undo():Boolean {
        transformContainer.commandFilterDelete(filter);
        return true;
    }
}
}