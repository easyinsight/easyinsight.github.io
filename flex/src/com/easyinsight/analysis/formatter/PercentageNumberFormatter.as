package com.easyinsight.analysis.formatter {

import mx.formatters.NumberBase;
import mx.formatters.NumberBaseRoundType;
import mx.formatters.NumberFormatter;
public class PercentageNumberFormatter extends NumberFormatter{
    public function PercentageNumberFormatter() {
        super();
    }

    override public function format(value:Object):String {
        // Reset any previous errors.
        if (error)
			error = null;

        if (useThousandsSeparator &&
			((decimalSeparatorFrom == thousandsSeparatorFrom) ||
			 (decimalSeparatorTo == thousandsSeparatorTo)))
        {
            error = defaultInvalidFormatError;
            return "";
        }

        if (decimalSeparatorTo == "" || !isNaN(Number(decimalSeparatorTo)))
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

        if (value === null || isNaN(Number(value)))
        {
            error = defaultInvalidValueError;
            return "";
        }

        // -- format --

        var isNegative:Boolean = (Number(value) < 0);

        var numStr:String = value.toString();
        var numArrTemp:Array = numStr.split(".");
        var numFraction:int = numArrTemp[1] ? String(numArrTemp[1]).length : 0;

        var workingPrecision:int;

        if (numFraction == 0) {
            workingPrecision = 0;
        } else {
            workingPrecision = int(precision);
        }

        if (workingPrecision <= numFraction)
		{
            if (rounding != NumberBaseRoundType.NONE)
			{
                numStr = dataFormatter.formatRoundingWithPrecision(
					numStr, rounding, int(workingPrecision));
			}
		}

        var numValue:Number = Number(numStr);
        if (Math.abs(numValue) >= 1)
        {
            numArrTemp = numStr.split(".");
            var front:String = useThousandsSeparator ?
							   dataFormatter.formatThousands(String(numArrTemp[0])) :
							   String(numArrTemp[0]);
            if (numArrTemp[1] != null && numArrTemp[1] != "")
                numStr = front + decimalSeparatorTo + numArrTemp[1];
            else
                numStr = front;
        }
        else if (Math.abs(numValue) > 0)
        {
        	// Check if the string is in scientific notation
        	if (numStr.indexOf("e") != -1)
        	{
	        	var temp:Number = Math.abs(numValue) + 1;
	        	numStr = temp.toString();
        	}
            numStr = decimalSeparatorTo +
					 numStr.substring(numStr.indexOf(".") + 1);
        }

        numStr = dataFormatter.formatPrecision(numStr, int(workingPrecision));

		// If our value is 0, then don't show -0
		if (Number(numStr) == 0)
		{
			isNegative = false;
		}

        if (isNegative)
            numStr = dataFormatter.formatNegative(numStr, useNegativeSign as Boolean);

        if (!dataFormatter.isValid)
        {
            error = defaultInvalidFormatError;
            return "";
        }

        return numStr + "%";
        
    }
}
}