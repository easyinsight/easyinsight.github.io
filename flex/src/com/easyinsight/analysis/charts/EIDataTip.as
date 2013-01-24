////////////////////////////////////////////////////////////////////////////////
//
//  Copyright (C) 2003-2006 Adobe Macromedia Software LLC and its licensors.
//  All Rights Reserved. The following is Source Code and is subject to all
//  restrictions on such code as contained in the End User License Agreement
//  accompanying this product.
//
////////////////////////////////////////////////////////////////////////////////

package com.easyinsight.analysis.charts
{

import flash.display.DisplayObject;
import flash.display.Graphics;
import flash.geom.Rectangle;
import flash.text.TextFieldAutoSize;
import flash.text.TextFormat;
import mx.charts.HitData;
import mx.charts.chartClasses.DataTip;
import mx.charts.chartClasses.GraphicsUtilities;
import mx.charts.styles.HaloDefaults;
import mx.core.IDataRenderer;
import mx.core.IUITextField;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.events.FlexEvent;
import mx.graphics.IFill;
import mx.graphics.SolidColor;
import mx.graphics.Stroke;
import mx.styles.CSSStyleDeclaration;

//--------------------------------------
//  Events
//--------------------------------------

/**
 *  Dispatched when an object's state changes from visible to invisible.
 *
 *  @eventType mx.events.FlexEvent.HIDE
 */
[Event(name="hide", type="mx.events.FlexEvent")]

/**
 *  Dispatched when the component becomes visible.
 *
 *  @eventType mx.events.FlexEvent.SHOW
 */
[Event(name="show", type="mx.events.FlexEvent")]

//--------------------------------------
//  Styles
//--------------------------------------


/**
 *  Background color of the component.
 *  You can either have a <code>backgroundColor</code>
 *  or a <code>backgroundImage</code>, but not both.
 *  Note that some components, like a Button, do not have a background
 *  because they are completely filled with the button face or other graphics.
 *  The DataGrid control also ignores this style.
 *  The default value is <code>undefined</code>.
 *  If both this style and the backgroundImage style are undefined,
 *  the control has a transparent background.
 */
[Style(name="backgroundColor", type="uint", format="Color", inherit="no")]

/**
 *  Black section of a three-dimensional border,
 *  or the color section of a two-dimensional border.
 *  The following components support this style: Button, CheckBox, ComboBox,
 *  MenuBar, NumericStepper, ProgressBar, RadioButton, ScrollBar, Slider,
 *  and all components that support the <code>borderStyle</code> style.
 *  The default value depends on the component class;
 *  if not overriden for the class, it is <code>0xAAB3B3</code>.
 */
[Style(name="borderColor", type="uint", format="Color", inherit="no")]

/**
 *  Bounding box style.
 *  The possible values are <code>"none"</code>, <code>"solid"</code>,
 *  <code>"inset"</code> and <code>"outset"</code>.
 *  The default value is <code>"inset"</code>.
 *
 *  <p>Note: The <code>borderStyle</code> style is not supported by the
 *  Button control or the Panel container.
 *  To make solid border Panels, set the <code>borderThickness</code>
 *  property, and set the <code>dropShadow</code> property to
 *  <code>false</code> if desired.</p>
 */
[Style(name="borderStyle", type="String", enumeration="inset,outset,solid,none", inherit="no")]

/**
 *  Number of pixels between the datatip's bottom border and its content area.
 *  The default value is 0.
 */
[Style(name="paddingBottom", type="Number", format="Length", inherit="no")]

/**
 *  Number of pixels between the datatip's top border and its content area.
 *  The default value is 0.
 */
[Style(name="paddingTop", type="Number", format="Length", inherit="no")]

/**
 *  Bottom inside color of a button's skin.
 *  A section of the three-dimensional border.
 *  The default value is <code>0xEEEEEE</code> (light gray).
 */
[Style(name="shadowColor", type="uint", format="Color", inherit="yes")]

/**
 *  The DataTip control provides information
 *  about a data point to chart users.
 *  When a user moves their mouse over a graphical element, the DataTip
 *  control displays text that provides information about the element.
 *  You can use DataTip controls to guide users as they work with your
 *  application or customize the DataTips to provide additional functionality.
 *
 *  <p>To enable DataTips on a chart, set its <code>showDataTips</code>
 *  property to <code>true</code>.</p>
 *
 */
public class EIDataTip extends DataTip {
{


    public function EIDataTip()
    {
        super();

        mouseChildren = false;
        mouseEnabled = false;
    }


    override protected function createChildren():void
    {
        super.createChildren();
        setStyle("fontSize", 10);
        setStyle("fontFamily", "Verdana");
    }
}

}
}
