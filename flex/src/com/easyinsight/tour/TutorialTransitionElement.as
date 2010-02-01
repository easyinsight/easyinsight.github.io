package com.easyinsight.tour {
public class TutorialTransitionElement extends TutorialElement {
    public function TutorialTransitionElement(nextTutorial:String) {
        super();
        this.nextTutorial = nextTutorial;
    }

    private var nextTutorial:String;
}
}