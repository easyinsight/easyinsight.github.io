package com.easyinsight.analysis {
import com.easyinsight.commands.ICommand;

public class DropAreaSwapCommand implements ICommand {
    private var sourceDropArea:DropArea;
    private var targetDropArea:DropArea;

    public function DropAreaSwapCommand(sourceDropArea:DropArea, targetDropArea:DropArea) {
        this.sourceDropArea = sourceDropArea;
        this.targetDropArea = targetDropArea;
    }

    public function execute():void {
        var transferItem:AnalysisItem = targetDropArea.analysisItem;
        targetDropArea.analysisItem = sourceDropArea.analysisItem;
        sourceDropArea.analysisItem = transferItem;
        targetDropArea.dispatchEvent(new DropAreaUpdateEvent(targetDropArea.analysisItem, transferItem));
    }

    public function undo():Boolean {
        return false;
    }
}
}