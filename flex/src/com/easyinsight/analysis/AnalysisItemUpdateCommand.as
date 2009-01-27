package com.easyinsight.analysis {
import com.easyinsight.commands.ICommand;
public class AnalysisItemUpdateCommand implements ICommand {

    private var previousAnalysisItem:AnalysisItem;
    private var newAnalysisItem:AnalysisItem;

    public function AnalysisItemUpdateCommand(previousAnalysisItem:AnalysisItem, newAnalysisItem:AnalysisItem) {
        this.previousAnalysisItem = previousAnalysisItem;
        this.newAnalysisItem = newAnalysisItem;
    }

    public function execute():void {
        
    }

    public function undo():Boolean {
        return false;
    }
}
}