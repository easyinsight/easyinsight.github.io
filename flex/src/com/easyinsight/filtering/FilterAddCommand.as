package com.easyinsight.filtering {
import com.easyinsight.commands.ICommand;
public class FilterAddCommand implements ICommand {
    private var transformContainer:TransformContainer;
    private var filter:IFilter;


    public function FilterAddCommand(transformContainer:TransformContainer, filter:IFilter) {
        this.transformContainer = transformContainer;
        this.filter = filter;
    }

    public function execute():void {
        transformContainer.commandFilterAdd(filter);
    }

    public function undo():Boolean {
        transformContainer.commandFilterDelete(filter);
        return true;
    }
}
}