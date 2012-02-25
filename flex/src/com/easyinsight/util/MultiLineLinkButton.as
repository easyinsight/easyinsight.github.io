/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/16/11
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.display.DisplayObject;
import flash.text.TextLineMetrics;

import mx.controls.Button;
import mx.controls.LinkButton;
import mx.core.IFlexDisplayObject;
import mx.core.mx_internal;

use namespace mx_internal;

public class MultiLineLinkButton extends LinkButton
{
    public function MultiLineLinkButton()
    {
        super();
    }

    override protected function createChildren() : void
    {
        if (  !textField  )
        {
            textField = new NoTruncationUITextField();
            textField.styleName = this;
            addChild(  DisplayObject(  textField  )  );
        }

        super.createChildren();

        textField.multiline = true;
        textField.wordWrap = true;
        textField.width = width;
    }

    override protected function measure() : void
    {
        if (  !isNaN(  explicitWidth  )  )
        {
            var tempIcon : IFlexDisplayObject = getCurrentIcon();
            var w : Number = explicitWidth;
            if (  tempIcon  )
                w -= tempIcon.width + getStyle(  "horizontalGap"  ) + getStyle(  "paddingLeft"  ) + getStyle(  "paddingRight"  );
            textField.width = w;
        }
        super.measure();

    }

    override public function measureText(  s : String  ) : TextLineMetrics
    {
        textField.text = s;
        var lineMetrics : TextLineMetrics = textField.getLineMetrics(  0  );
        lineMetrics.width = textField.textWidth + 4;
        lineMetrics.height = textField.textHeight + 4;
        return lineMetrics;
    }
}
}

import mx.core.UITextField;

class NoTruncationUITextField extends UITextField
{
    public function NoTruncationUITextField()
    {
        super();
    }

    override public function truncateToFit(  s : String = null  ) : Boolean
    {
        return false;
    }
}
