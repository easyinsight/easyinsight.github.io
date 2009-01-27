package com.easyinsight.filtering {
import com.easyinsight.commands.ICommand;
public class FilterDeleteCommand implements ICommand {
    private var transformContainer:TransformContainer;
    private var filter:IFilter;


    public function FilterDeleteCommand(transformContainer:TransformContainer, filter:IFilter) {
        this.transformContainer = transformContainer;
        this.filter = filter;
    }

    public function execute():void {
        transformContainer.commandFilterDelete(filter);
    }

    public function undo():Boolean {
        transformContainer.commandFilterAdd(filter);
        return true;
    }
}
}