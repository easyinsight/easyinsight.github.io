/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/31/12
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.display.Graphics;
import flash.geom.Matrix;

import mx.controls.Alert;
import mx.core.EdgeMetrics;

import mx.skins.ProgrammaticSkin;
import mx.skins.halo.HaloBorder;
import mx.utils.GraphicsUtil;

public class GradientBackground extends HaloBorder {
    public function GradientBackground() {
        super();
    }

    private var topCornerRadius:Number;        // top corner radius
    private var bottomCornerRadius:Number;    // bottom corner radius
    private var fillColors:Array;            // fill colors (two)
    private var setup:Boolean;

    // ------------------------------------------------------------------------------------- //

    private function setupStyles():void
    {
        fillColors = getStyle("fillColors") as Array;
        if (!fillColors) fillColors = [0xFFFFFF, 0xFFFFFF];

        topCornerRadius = getStyle("cornerRadius") as Number;
        if (!topCornerRadius) topCornerRadius = 0;

        bottomCornerRadius = getStyle("bottomCornerRadius") as Number;
        if (!bottomCornerRadius) bottomCornerRadius = topCornerRadius;

    }

    // ------------------------------------------------------------------------------------- //

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
    {
        super.updateDisplayList(unscaledWidth, unscaledHeight);

        setupStyles();

        var g:Graphics = graphics;
        var b:EdgeMetrics = borderMetrics;
        var w:Number = unscaledWidth - b.left - b.right;
        var h:Number = 38;
        var m:Matrix = verticalGradientMatrix(0, 0, w, h);

        g.beginGradientFill("linear", fillColors, [1, 1], [0, 255], m);

        var tr:Number = Math.max(topCornerRadius-2, 0);
        var br:Number = Math.max(bottomCornerRadius-2, 0);
        GraphicsUtil.drawRoundRectComplex(g, b.left, b.top, w, h, tr, tr, 0, 0);
        g.endFill();

    }
}
}
