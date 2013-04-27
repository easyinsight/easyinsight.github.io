package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;

import java.text.NumberFormat;

/**
 * User: jamesboe
 * Date: 4/9/13
 * Time: 2:36 PM
 */
public class DecimalDegrees extends Function {
    public Value evaluate() {
        try {
            char c = 176;
            String blah = params.get(0).toString().trim();
            String degrees = blah.substring(0, blah.indexOf(c)).trim();
            String minutes = blah.substring(blah.indexOf(c) + 1, blah.indexOf("'")).trim();
            String seconds = blah.substring(blah.indexOf("'") + 1, blah.length() - 1).trim();
            char direction = blah.charAt(blah.length() - 1);
            System.out.println(degrees + " - " + minutes + " - " + seconds + " - " + direction);
            double d = Double.parseDouble(degrees);
            double m = Double.parseDouble(minutes) / 60.0;
            double s = Double.parseDouble(seconds) / 3600.0;
            double result = d + m + s;
            if (direction == 'S' || direction == 'W') {
                result *= -1;
            }
            NumberFormat dFormat = NumberFormat.getInstance();
            dFormat.setMaximumFractionDigits(6);
            String str = dFormat.format(result);
            return new StringValue(str);
        } catch (Exception e) {
            e.printStackTrace();
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return 1;
    }
}
