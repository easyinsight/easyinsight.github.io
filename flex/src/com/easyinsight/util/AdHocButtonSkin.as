////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2005-2006 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////

package com.easyinsight.util
{

import mx.core.EdgeMetrics;
import mx.skins.Border;

/**
 *  The skin for all the states of a LinkButton.
 */
public class AdHocButtonSkin extends Border
{


    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------

    /**
     *  Constructor.
     */
    public function AdHocButtonSkin()
    {
        super();
    }

    //--------------------------------------------------------------------------
    //
    //  Overridden properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    //  borderMetrics
    //----------------------------------

    /**
     *  @private
     */
    override public function get borderMetrics():EdgeMetrics
    {
        return EdgeMetrics.EMPTY;
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

        var cornerRadius:Number = getStyle("cornerRadius");
        var rollOverColor:uint = getStyle("rollOverColor");
        var selectionColor:uint = getStyle("selectionColor");

        var adHocSelected:Boolean = getStyle("adHocSelected");

        graphics.clear();

        switch (name)
        {
            case "upSkin":
            {
                // Draw invisible shape so we have a hit area.
                if (adHocSelected) {
                    drawRoundRect(
                            0, 0, w, h, cornerRadius,
                            0, .35);
                } else {
                    drawRoundRect(
                            0, 0, w, h, cornerRadius,
                            0, 0);
                }
                break;
            }

            case "overSkin":
            {
                drawRoundRect(
                        0, 0, w, h, cornerRadius,
                        rollOverColor, 1);
                break;
            }

            case "downSkin":
            {
                drawRoundRect(
                        0, 0, w, h, cornerRadius,
                        selectionColor, 1);
                break;
            }

            case "disabledSkin":
            {
                // Draw invisible shape so we have a hit area.
                drawRoundRect(
                        0, 0, w, h, cornerRadius,
                        0, 0);
                break;
            }
        }
    }
}

}
