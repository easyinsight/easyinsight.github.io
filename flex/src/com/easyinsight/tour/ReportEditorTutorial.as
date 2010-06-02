package com.easyinsight.tour {
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.solutions.Solution;

import mx.collections.ArrayCollection;

public class ReportEditorTutorial extends TutorialFramework {

    private var _solution:Solution;

    public function ReportEditorTutorial(solution:Solution = null) {
        super();
        this._solution = solution;
    }

    override protected function createNotes():ArrayCollection {
        var notes:ArrayCollection = new ArrayCollection();
        //notes.addItem(new SetNoteProperty("reportEditor", "enabled", false));
        notes.addItem(new ScreenNoteAnchor("This is the report editor, where you can create a wide variety of reports and slice and dice your way through data.", "center", 90));        
        //notes.addItem(new SetNoteProperty("coreVBox", "enabled", false));
        notes.addItem(new ActionTutorialElement(new NavigationEvent("My Data")));
        notes.addItem(new ScreenNoteAnchor("The My Data page is where you'll find a list of your data sources and reports. You can access the report editor from here, as well as opening any reports you may have created.", "center", 150));
        notes.addItem(new ActionTutorialElement(new NavigationEvent("Exchange", null, {viewMode: 1, displayMode: 0, solution: _solution})));
        notes.addItem(new ScreenNoteAnchor("The Exchange provides you with the community for sharing data and reports.", "center", 80));                
        notes.addItem(new InstallReportTutorialElement(_solution.solutionID));
        notes.addItem(new ScreenNoteAnchor("What you see here is the report view, displaying the report template chosen a moment ago against your data set.", "center", 100));
        notes.addItem(new ScreenNoteAnchor("You can export the report to a variety of locations, view in full screen for demonstration purposes, or adjust any filters on the report.", "center", 120));
        notes.addItem(new ScreenNoteAnchor("If you feel like a particular report helps provide you with meaningful insight, you can add it to your own My Data page for future reference, as well as enabling you to change it however you like. Clicking on the Add to My Data button here will accomplish this task.", "center", 200));
        notes.addItem(new ActionTutorialElement(new NavigationEvent("Home")));
        notes.addItem(new ScreenNoteAnchor("Here, we've returned to the Home page, which now shows you the scorecard we created. As you add reports, you can access those reports from the popup menu that appears when you left click on a KPI.", "center", 150, "Close Tutorial"));        

        /*notes.addItem(new NoteAnchor("tabNavigator", "The metadata tab provides settings around saving and sharing the report with others."));
        notes.addItem(new NoteAnchor("tabNavigator", "The data tab focuses around the data fields of the report."));
        notes.addItem(new NoteAnchor("tabNavigator", "The filters tab provides you wit."));*/        

        return notes;
    }
}
}