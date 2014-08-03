/**
 * Created by jamesboe on 8/1/14.
 */
package com.easyinsight.util {
import flash.display.Graphics;
import flash.filters.DropShadowFilter;
import flash.geom.Rectangle;

import mx.core.UIComponent;
import mx.graphics.SolidColor;

public class FlatButtonSkin extends UIComponent {
    public function FlatButtonSkin() {
    }

        protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
            super.updateDisplayList(unscaledWidth,unscaledHeight);
            var w:Number = unscaledWidth;
            var h:Number = unscaledHeight;

            // hold the values of the gradients depending on button state
            var backgroundFillColor:Number;
            var backgroundFillColor2:Number;

            var fill:SolidColor = new SolidColor();
            fill.color = 0x0084b4;

            // reference the graphics object of this skin class
            var g:Graphics = graphics;
            g.clear();

            //  which skin is the button currently looking for? Which skin to use?
            switch (name) {
                case "upSkin":
                    backgroundFillColor = 0x929292;
                    backgroundFillColor2 = 0x000000;
                    break;
                case "overSkin":
                    backgroundFillColor = 0x696969;
                    backgroundFillColor2 = 0x504F4F;
                    break;
                case "downSkin":
                    backgroundFillColor = 0x888888;
                    backgroundFillColor2 = 0x777777;
                    color: 0xFF0000;
                    break;
                case "disabledSkin":
                    backgroundFillColor = 0xCCCCCC;
                    backgroundFillColor2 = 0xCCCCCC;
                    break;
            }
            // depending on which state the button's in, we set our color for the
            // gradients on the skin


            /*fill. = [g1,g2];
            fill.angle = 90;*/
            // fill the rectangle
            g.moveTo(0,0);
            g.beginFill(0x555555);
            g.drawRoundRect(0, 0, w, h, 4, 4);
            //fill.begin(g,new Rectangle(0,0,w,h));
            /*g.lineTo(w,0);
            g.lineTo(w,h);
            g.lineTo(0,h);
            g.lineTo(0,0);*/
            g.endFill();
            fill.end(g);
            // if we're not showing the down skin, show the shadow. Otherwise hide it on the "down state" to look like it's being pressed
    }
}
}
