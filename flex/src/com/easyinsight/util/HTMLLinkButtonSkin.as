////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2005-2007 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////

package com.easyinsight.util
{

import flash.display.DisplayObject;
import flash.display.GradientType;

import mx.controls.Alert;
import mx.core.IFlexDisplayObject;
import mx.core.UIComponent;
import mx.core.mx_internal;
import mx.skins.halo.PopUpIcon;
import mx.styles.StyleManager;
import mx.utils.ColorUtil;
import mx.core.IProgrammaticSkin;

/**
 *  The skin for all the states of a PopUpButton.
 */
public class HTMLLinkButtonSkin extends UIComponent implements IProgrammaticSkin
{

    //--------------------------------------------------------------------------
    //
    //  Class variables
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     */
    private static var cache:Object = {};

    //--------------------------------------------------------------------------
    //
    //  Class methods
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     *  Several colors used for drawing are calculated from the base colors
     *  of the component (themeColor, borderColor and fillColors).
     *  Since these calculations can be a bit expensive,
     *  we calculate once per color set and cache the results.
     */
    /*private static function calcDerivedStyles(themeColor:uint,
                                              fillColor0:uint,
                                              fillColor1:uint):Object
    {
        var key:String = HaloColors.getCacheKey(themeColor,
                fillColor0, fillColor1);

        if (!cache[key])
        {
            var o:Object = cache[key] = {};

            // Cross-component styles.
            HaloColors.addHaloColors(o, themeColor, fillColor0, fillColor1);
        }

        return cache[key];
    }*/

    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------

    /**
     *  Constructor.
     */
    public function HTMLLinkButtonSkin()
    {
        super();

        mouseEnabled = false;
    }

    //--------------------------------------------------------------------------
    //
    //  Overridden properties
    //
    //--------------------------------------------------------------------------


    //----------------------------------
    //  measuredWidth
    //----------------------------------

    /**
     *  @private
     */
    override public function get measuredWidth():Number
    {
        return DEFAULT_MEASURED_MIN_WIDTH;
    }

    //----------------------------------
    //  measuredHeight
    //----------------------------------

    /**
     *  @private
     */
    override public function get measuredHeight():Number
    {
        return DEFAULT_MEASURED_MIN_HEIGHT;
    }

    //--------------------------------------------------------------------------
    //
    //  Overridden methods
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     */
    override protected function updateDisplayList(w:Number, h:Number):void
    {
        super.updateDisplayList(w, h);

        // User-defined styles.
        var arrowColor:uint = getStyle("iconColor");
        var borderColor:uint = getStyle("borderColor");
        var cornerRadius:Number = getStyle("cornerRadius");
        var fillAlphas:Array = getStyle("fillAlphas");
        var fillColors:Array = getStyle("fillColors");
        StyleManager.getColorNames(fillColors);
        var highlightAlphas:Array = getStyle("highlightAlphas");
        var themeColor:uint = getStyle("themeColor");

        var overThemeColor:Number =
                ColorUtil.adjustBrightness2(themeColor, -25);

        // Derivative styles.
        /*var derStyles:Object = calcDerivedStyles(themeColor, fillColors[0],
                fillColors[1]);*/

        var borderColorDrk1:Number =
                borderColor;

        var themeColorDrk1:Number =
                themeColor;

        var cr:Number = Math.max(0, cornerRadius);
        var cr1:Number = Math.max(0, cornerRadius - 1);

        var upFillColors:Array;
        var upFillAlphas:Array;

        var overFillColors:Array;
        var overFillAlphas:Array;

        graphics.clear();

        var dividerPosX:int = 0;
        var arrowButtonWidth:int = 0;

        switch (name)
        {
            case "upSkin":
            {
                upFillColors = [ fillColors[0], fillColors[1] ];
                upFillAlphas = [ fillAlphas[0], fillAlphas[1] ];

                // button border/edge
                drawRoundRect(
                        0, 0, w, h, cr,
                        [ borderColor, borderColorDrk1 ], 1,
                        verticalGradientMatrix(0, 0, w, h),
                        GradientType.LINEAR, null,
                        { x: 1, y: 1, w: w - 2, h: h - 2, r: cornerRadius - 1 });

                drawRoundRect(
                        dividerPosX, 1, 1, h - 2, 0,
                        [ borderColor, borderColorDrk1 ], 1,
                        verticalGradientMatrix(0, 0, w, h));

                // button fill
                drawRoundRect(
                        1, 1, w - 2, h - 2, cr1,
                        upFillColors, upFillAlphas,
                        verticalGradientMatrix(1, 1, w - 2, h - 2),
                        GradientType.LINEAR, null,
                        { x: dividerPosX, y: 1, w: 1, h: h - 2, r: 0 });

                // top highlight
                /*drawRoundRect(
                        1, 1, w - 2, (h - 2) / 2,
                        { tl: cr1, tr: cr1, bl: 0, br: 0 },
                        [ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
                        verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2),
                        GradientType.LINEAR, null,
                        { x: dividerPosX, y: 1, w: 1, h: (h - 2) / 2, r: 0 });*/

                // side bevel highlight edges
                drawRoundRect(
                                dividerPosX - 1, 1, 1, h - 2, 0,
                        borderColor, 0);
                drawRoundRect(
                                dividerPosX + 1, 1, 1, h - 2, 0,
                        borderColor, 0);

                break;
            }

            case "overSkin": // for hover on the main button (left) side
            {

                upFillColors = [ overThemeColor, overThemeColor ];
                upFillAlphas = [ fillAlphas[0], fillAlphas[1] ];



                if (fillColors.length > 2)
                    overFillColors = [ overThemeColor, overThemeColor ];
                else
                    overFillColors = [ overThemeColor, overThemeColor ];

                if (fillAlphas.length > 2)
                    overFillAlphas = [ fillAlphas[0], fillAlphas[1] ];

                // button border/edge
                drawRoundRect(
                        0, 0, w, h, cr,
                        [ overThemeColor, overThemeColor ], 1,
                        verticalGradientMatrix(0, 0, w, h),
                        GradientType.LINEAR, null,
                        { x: 1, y: 1, w: w - 2, h: h - 2, r: cornerRadius - 1 });

                drawRoundRect(
                        dividerPosX, 1, 1, h - 2, 0,
                        [ overThemeColor, overThemeColor ], 1,
                        verticalGradientMatrix(0, 0, w, h));
                // button fill
                drawRoundRect(
                        1, 1, w - 2, h - 2, cr1,
                        upFillColors, upFillAlphas,
                        verticalGradientMatrix(1, 1, w - 2, h - 2),
                        GradientType.LINEAR, null,
                        { x: dividerPosX, y: 1, w: 1, h: h - 2,
                            r: getRadius(cr1, true) });

                // left/main button fill
                drawRoundRect(
                        1, 1, w - arrowButtonWidth - 2, h - 2,
                        getRadius(cr1, true),
                        overFillColors, overFillAlphas,
                        verticalGradientMatrix(1, 1, dividerPosX - 2, h - 2));

                // top highlight
                /*drawRoundRect(
                        1, 1, w - 2, (h -2) / 2,
                        { tl: cr1, tr: cr1, bl: 0, br: 0 },
                        [ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
                        verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2),
                        GradientType.LINEAR, null,
                        { x: dividerPosX, y: 1, w: 1, h: (h - 2) / 2, r: 0 });*/

                // side bevel highlight edges
                drawRoundRect(
                                dividerPosX - 1, 1, 1, h - 2, 0,
                        overThemeColor, 0.35);

                break;
            }

            case "downSkin": // for press on the main button (left) side
            {
                upFillColors = [ fillColors[0], fillColors[1] ];
                upFillAlphas = [ fillAlphas[0], fillAlphas[1] ];

                // button border/ddge
                drawRoundRect(
                        0, 0, w, h, cr,
                        [ themeColor, themeColorDrk1 ], 1,
                        verticalGradientMatrix(0, 0, w, h ),
                        GradientType.LINEAR, null,
                        { x: 1, y: 1, w: w - 2, h: h - 2, r: cornerRadius - 1 });

                drawRoundRect(
                        dividerPosX, 1, 1, h - 2, 0,
                        [ themeColor, themeColorDrk1 ], 1,
                        verticalGradientMatrix(0, 0, w, h));

                // button fill
                drawRoundRect(
                        1, 1, w - 2, h - 2, cr1,
                        upFillColors, upFillAlphas,
                        verticalGradientMatrix(1, 1, w - 2, h - 2),
                        GradientType.LINEAR, null,
                        { x: dividerPosX, y: 1, w: 1, h: h - 2, r: 0 });

                // left/main button fill
                drawRoundRect(
                        1, 1, w - arrowButtonWidth - 2, h - 2,
                        getRadius(cr1, true),
                        [ 0x0, 0x0], 1,
                        verticalGradientMatrix(1, 1, dividerPosX - 2, h - 2));

                // top highlight (checked, works)
                /*drawRoundRect(
                        1, 1, w - 2, (h -2) / 2,
                        { tl: cr1, tr: cr1, bl: 0, br: 0 },
                        [ 0xFFFFFF, 0xFFFFFF ], highlightAlphas,
                        verticalGradientMatrix(1, 1, w - 2, (h - 2) / 2),
                        GradientType.LINEAR, null,
                        { x: dividerPosX, y: 1, w: 1, h: (h -2) / 2, r: 0 });*/

                // side bevel highlight edges
                drawRoundRect(
                                dividerPosX - 1, 1, 1, h - 2, 0,
                        themeColorDrk1, 0.3);
                drawRoundRect(
                                dividerPosX + 1, 1, 1, h - 2, 0,
                        borderColor, 0);

                break;
            }

            case "disabledSkin":
            {
                arrowColor = getStyle("disabledIconColor");

                var disFillColors:Array = [ fillColors[0], fillColors[1] ];

                var disFillAlphas:Array =
                        [ Math.max(0, fillAlphas[0] - 0.15),
                            Math.max(0, fillAlphas[1] - 0.15) ];

                // outer edge
                drawRoundRect(
                        0, 0, w, h, cornerRadius,
                        [ borderColor, borderColorDrk1 ], 0.5,
                        verticalGradientMatrix(0, 0, w, h ),
                        GradientType.LINEAR, null,
                        { x: 1, y: 1, w: w - 2, h: h - 2, r: cornerRadius - 1 });

                drawRoundRect(
                        dividerPosX, 1, 1, h - 2, 0,
                        [ borderColor, borderColorDrk1 ], 0.5);

                // button fill
                drawRoundRect(
                        1, 1, w - 2, h - 2, cr1,
                        disFillColors, disFillAlphas,
                        verticalGradientMatrix(1, 1, w - 2, h - 2),
                        null, null,
                        { x: dividerPosX, y: 1, w: 1, h: h - 2, r: 0 });

                break;
            }
        }
    }

    //--------------------------------------------------------------------------
    //
    //  Methods
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     */
    private function getRadius(r:Number, left:Boolean):Object
    {
        return left ?
        { br: 0, bl: r, tr: 0, tl: r } :
        { br: r, bl: 0, tr: r, tl: 0 };
    }
}

}
