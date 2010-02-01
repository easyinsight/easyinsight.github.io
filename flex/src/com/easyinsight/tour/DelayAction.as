package com.easyinsight.tour {
import flash.events.TimerEvent;
import flash.utils.Timer;

public class DelayAction extends TutorialElement {

    private var timer:Timer;

    public function DelayAction() {
        super();
    }

    override public function staysOnScreen():Boolean {
        return true;
    }

    override public function forwardExecute():void {
        timer = new Timer(500);
        timer.addEventListener(TimerEvent.TIMER, onTimer);
        timer.start();
    }

    private function onTimer(event:TimerEvent):void {
        timer.stop();
        timer.removeEventListener(TimerEvent.TIMER, onTimer);
        timer = null;
        dispatchEvent(new NoteEvent(NoteEvent.NEXT_NOTE, null));
    }
}
}