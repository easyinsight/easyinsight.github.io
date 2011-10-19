package com.easyinsight.util {
import flash.display.Stage;
import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.ui.Keyboard;

import mx.containers.VBox;
import mx.effects.Effect;
import mx.effects.Fade;
import mx.events.EffectEvent;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class EISlimWindow extends VBox {

    private var eiStage:Stage;

    public function EISlimWindow() {
        addEventListener(FlexEvent.CREATION_COMPLETE, onCreation);
        addEventListener(Event.REMOVED, onRemove);
    }

    private function onRemove(event:Event):void {
        if (eiStage != null) {
            eiStage.removeEventListener(KeyboardEvent.KEY_UP, onKey);
            eiStage = null;
        }
    }

    public function close():void {
        var fadeEffect:Effect = new Fade(this);
        fadeEffect.duration = 500;
        Fade(fadeEffect).alphaFrom = 1;
        Fade(fadeEffect).alphaTo = 0;
        fadeEffect.addEventListener(EffectEvent.EFFECT_END, onEffectEnd);
        fadeEffect.play();
    }

    private function onEffectEnd(event:Event):void {
        if (eiStage != null) {
            eiStage.removeEventListener(KeyboardEvent.KEY_UP, onKey);
            eiStage = null;
        }
        PopUpManager.removePopUp(this);
    }

    private function onCreation(event:FlexEvent):void {
        eiStage = stage;
        stage.addEventListener(KeyboardEvent.KEY_UP, onKey, false, 0, true);
        var resizeEffect:Effect = new Fade(this);
        resizeEffect.duration = 500;
        Fade(resizeEffect).alphaFrom = 0;
        Fade(resizeEffect).alphaTo = 1;
        resizeEffect.play();
    }

    private function onKey(event:KeyboardEvent):void {
        if (event.keyCode == Keyboard.ESCAPE) {
            close();
        }
    }
}
}