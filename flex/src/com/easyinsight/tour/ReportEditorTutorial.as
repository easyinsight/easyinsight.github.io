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
        notes.addItem(new SetNoteProperty("reportEditor", "enabled", false));
        notes.addItem(new ScreenNoteAnchor("This is the report editor, where you can create a wide variety of reports and slice and dice your way through data.", "center", 90));
        notes.addItem(new NoteAnchor("Measure", "You can see the list of fields available to you for building reports in this list down the left hand side of the report editor.", false));
        notes.addItem(new NoteAnchor("_controlBar", "These are the drop areas. You can construct a report by dragging fields from the left into these drop areas."));
        notes.addItem(new NoteAnchor("_controlBar", "Once you drag a field into the drop area, you can click the Edit button next to the field to change properties of the field--changing a Sum to an Average, a Grouping to a Range, and so on."));
        notes.addItem(new NoteAnchor("analysisState", "This dropdown box allows you to choose the type of report you're creating."));
        notes.addItem(new NoteAnchor("tabNavigator", "The tabs provide you with different key sets of functionality around manipulating reports."));
        notes.addItem(new SetNoteProperty("tabNavigator", "selectedIndex", 0));
        notes.addItem(new NoteAnchor("reportMetadataTab", "The metadata tab is where you set attributes on the report such as the report name, tags, and description. This tab is also where you configure report visibility--making a report visible to anyone, making the template of the report available to other users, and so on.", true, true, "top", 190));
        notes.addItem(new SetNoteProperty("tabNavigator", "selectedIndex", 1));
        notes.addItem(new DelayAction());
        notes.addItem(new NoteAnchor("reportDataTab", "The data tab provides a variety of options for augmenting the basic fields of the data source. You can add hierarchies to enable rollup and drilldown across the data or create calculated measures.", true, true, "top"));
        notes.addItem(new SetNoteProperty("tabNavigator", "selectedIndex", 2));
        notes.addItem(new DelayAction());
        notes.addItem(new NoteAnchor("transformContainer", "You need to be able to filter down on key sets of the data to find meaningful insights. Drag fields from the lefthand field list into the filter tab here to dynamically create a variety of filters on the report.", true, true, "top"));
        notes.addItem(new SetNoteProperty("tabNavigator", "selectedIndex", 3));
        notes.addItem(new DelayAction());
        notes.addItem(new NoteAnchor("scrubTab", "You might need to transform the data in the report to get the results you want. You can apply data scrubs from this tab to clean up and alter data as necessary.", true, true, "top"));
        notes.addItem(new SetNoteProperty("tabNavigator", "selectedIndex", 4));
        notes.addItem(new DelayAction());
        notes.addItem(new NoteAnchor("exportTab", "Once you have the report together, you need to be able to export it into other environments. The export tab provides you with the functionality to export to Excel, dashboards, and more.", true, true, "top"));
        var controlBarAnchor:NoteAnchor = new NoteAnchor("controlBar", "The control bar provides you with buttons to save reports, navigate to other views of the data source, and share the report with others.");
        controlBarAnchor.preferredTailPosition = "top";
        notes.addItem(controlBarAnchor);
        notes.addItem(new SetNoteProperty("reportEditor", "enabled", true));
        notes.addItem(new ScreenNoteAnchor("Go ahead and try creating your own reports. Once you're ready to move on to the next step of the tutorial, just click the next step button.", "center", 120));
        notes.addItem(new ScreenNoteAnchor("I'm ready to move on to the next step of the tutorial.", "upperright", 80));
        notes.addItem(new SetNoteProperty("coreVBox", "enabled", false));
        notes.addItem(new ActionTutorialElement(new NavigationEvent("My Data")));
        notes.addItem(new ScreenNoteAnchor("The My Data page is where you'll find a list of your data sources and reports. You can access the report editor from here, as well as opening any reports you may have created.", "center", 150));
        notes.addItem(new ActionTutorialElement(new NavigationEvent("Exchange")));
        notes.addItem(new ScreenNoteAnchor("The Exchange provides you with the community for sharing data and reports.", "center", 80));
        notes.addItem(new ActionTutorialElement(new NavigationEvent("Exchange", null, {viewMode: 1, displayMode: 0})));
        notes.addItem(new ScreenNoteAnchor("Particularly relevant to this tutorial is the Connection Exchange. This screen is where you'll find report templates contributed by other users against data sources.", "center", 150));
        notes.addItem(new ActionTutorialElement(new NavigationEvent("Exchange", null, {viewMode: 1, displayMode: 0, solution: _solution})));
        notes.addItem(new NoteAnchor("solutionBox", "Restricting the view to those reports created against " + _solution.name + ", you can see a variety of report options for you. Clicking on the name of one of the reports in this list will open it up, applying that report template to your data."));
        notes.addItem(new InstallReportTutorialElement(_solution.solutionID));
        notes.addItem(new ScreenNoteAnchor("What you see here is the report view, displaying the report template chosen a moment ago against your data set.", "center", 100));
        notes.addItem(new ScreenNoteAnchor("You can export the report to a variety of locations, view in full screen for demonstration purposes, or adjust any filters on the report.", "center", 120));
        notes.addItem(new ScreenNoteAnchor("If you feel like a particular report helps provide you with meaningful insight, you can add it to your own My Data page for future reference, as well as enabling you to change it however you like. Clicking on the Add to My Data button here will accomplish this task.", "center", 200));
        notes.addItem(new ActionTutorialElement(new NavigationEvent("Home")));
        notes.addItem(new ScreenNoteAnchor("Here, we've returned to the Home page, which now shows you the scorecard we created. As you add reports, you can access those reports from the popup menu that appears when you left click on a KPI.", "center", 150, "Close Tutorial"));
        notes.addItem(new SetNoteProperty("coreVBox", "enabled", true));

        /*notes.addItem(new NoteAnchor("tabNavigator", "The metadata tab provides settings around saving and sharing the report with others."));
        notes.addItem(new NoteAnchor("tabNavigator", "The data tab focuses around the data fields of the report."));
        notes.addItem(new NoteAnchor("tabNavigator", "The filters tab provides you wit."));*/        

        return notes;
    }
}
}