package com.easyinsight.preloader {
import flash.display.Loader;
import flash.utils.ByteArray;
import flash.events.MouseEvent;
import flash.events.TimerEvent;
import flash.utils.Timer;

public class WelcomeScreen extends Loader {
    [ Embed(source="../../../../assets/large_logo2.PNG", mimeType="application/octet-stream") ]
    public var WelcomeScreenGraphic:Class;
    public var timer:Timer;
    private var fadeInRate:Number = .01;
    private var fadeOutRate:Number = .02;
    private var moveRate:Number = 1;
    private var timeAutoClose:int = 500;
    public var ready:Boolean = false;
    public var makeVisible:Boolean = false;

    public function WelcomeScreen()
    {
        this.visible = false;
        this.alpha = 1;

        timer = new Timer(1);
        timer.addEventListener(TimerEvent.TIMER, updateView);
        timer.start();

        this.loadBytes(new WelcomeScreenGraphic() as ByteArray);
        this.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
    }

    public function updateView(event:TimerEvent):void
    {
        //if (this.alpha < 1)    this.alpha = this.alpha + this.fadeInRate;
        //if (this.alpha < 1)    this.alpha = this.alpha + this.fadeInRate;
        //if (!visible) {
            this.stage.addChild(this);
            this.x = this.stage.stageWidth / 2 - this.width / 2;
            this.y = this.stage.stageHeight / 2 - this.height / 2;
        if (!makeVisible) {
            makeVisible = true;
        } else {
            this.visible = true;
        }
        //}
        if (this.ready) closeScreen();
       // if (this.ready && timer.currentCount > this.timeAutoClose) closeScreen();
    }

    public function closeScreen():void
    {
        timer.stop();
        this.parent.removeChild(this);
        /*timer.removeEventListener(TimerEvent.TIMER, updateView);
        timer.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
        timer.addEventListener(TimerEvent.TIMER, closeScreenFade);*/
    }

    public function closeScreenFade(event:TimerEvent):void
    {
        timer.stop();
        this.parent.removeChild(this);
        /*if (x > 0 || y > 0) {
            if (x > 0) {
                x -= moveRate;
            }
            if (y > 0) {
                y -= moveRate;
            }
        } else {
            timer.stop();
            this.parent.removeChild(this);
        }*/
        /*if (this.alpha > 0) {
            this.alpha = this.alpha - fadeOutRate;
        } else {
            timer.stop();
            this.parent.removeChild(this);
        }*/
    }

    public function mouseDown(event:MouseEvent):void
    {
        closeScreen();
    }

}
}