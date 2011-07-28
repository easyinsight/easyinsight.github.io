/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/21/11
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.formatter {
import mx.formatters.CurrencyFormatter;
import mx.formatters.NumberBase;
import mx.formatters.NumberBaseRoundType;

public class EICurrencyFormatter extends CurrencyFormatter {
    public function EICurrencyFormatter() {
    }

    override public function format(value:Object):String
    {
        // Reset any previous errors.
        if (error)
			error = null;

        if (useThousandsSeparator &&
			(decimalSeparatorFrom == thousandsSeparatorFrom ||
			 decimalSeparatorTo == thousandsSeparatorTo))
        {
            error = defaultInvalidFormatError;
            return "";
        }

        if (decimalSeparatorTo == "")
        {
            error = defaultInvalidFormatError;
            return "";
        }

        var dataFormatter:NumberBase = new NumberBase(decimalSeparatorFrom,
													  thousandsSeparatorFrom,
													  decimalSeparatorTo,
													  thousandsSeparatorTo);

        // -- value --

        if (value is String)
            value = dataFormatter.parseNumberString(String(value));

        if (value === null || isNaN(Number(value)) || !isFinite(Number(value)))
        {
            error = defaultInvalidValueError;
            return "";
        }

        // -- format --

		var isNegative:Boolean = (Number(value) < 0);

        var numStr:String = value.toString();
        var numArrTemp:Array = numStr.split(".");
        var numFraction:int = numArrTemp[1] ? String(numArrTemp[1]).length : 0;

        if (precision <= numFraction)
		{
            if (rounding != NumberBaseRoundType.NONE)
			{
                numStr = dataFormatter.formatRoundingWithPrecision(
					numStr, rounding, int(precision));
			}
		}

        var numValue:Number = Number(numStr);
        if (Math.abs(numValue) >= 1)
        {
            numArrTemp = numStr.split(".");
            var front:String =
				useThousandsSeparator ?
				dataFormatter.formatThousands(String(numArrTemp[0])) :
				String(numArrTemp[0]);
            if (numArrTemp[1] != null && numArrTemp[1] != "")
                numStr = front + decimalSeparatorTo + numArrTemp[1];
            else
                numStr = front;
        }
        else if (Math.abs(numValue) > 0)
        {
        	// if the value is in scientefic notation then the search for '.'
        	// doesnot give the correct result. Adding one to the value forces
        	// the value to normal decimal notation.
        	// As we are dealing with only the decimal portion we need not
        	// worry about reverting the addition
        	if (numStr.indexOf("e") != -1)
        	{
	        	var temp:Number = Math.abs(numValue) + 1;
	        	numStr = temp.toString();
        	}
            numStr = decimalSeparatorTo +
					 numStr.substring(numStr.indexOf(".") + 1);
        }

        numStr = dataFormatter.formatPrecision(numStr, int(precision));

		// If our value is 0, then don't show -0
		if (Number(numStr) == 0)
		{
			isNegative = false;
		}

        if (isNegative)
            numStr = dataFormatter.formatNegative(numStr, useNegativeSign);

        if (!dataFormatter.isValid)
        {
            error = defaultInvalidFormatError;
            return "";
        }

        // -- currency --

        if (alignSymbol == "left")
		{
            if (isNegative)
			{
                var nSign:String = numStr.charAt(0);
                var baseVal:String = numStr.substr(1, numStr.length - 1);
                numStr = nSign + currencySymbol + baseVal;
            }
			else
			{
                numStr = currencySymbol + numStr;
            }
        }
		else if (alignSymbol == "right")
		{
            var lastChar:String = numStr.charAt(numStr.length - 1);
            if (isNegative && lastChar == ")")
			{
                baseVal = numStr.substr(0, numStr.length - 1);
                numStr = baseVal + currencySymbol + lastChar;
            }
			else
			{
                numStr = numStr + currencySymbol;
            }
        }
		else
		{
            error = defaultInvalidFormatError;
            return "";
        }

        return numStr;
    }
}
}
