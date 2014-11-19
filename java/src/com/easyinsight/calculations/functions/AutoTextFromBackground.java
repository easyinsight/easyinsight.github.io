package com.easyinsight.calculations.functions;

import cern.jet.stat.Descriptive;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.TextValueExtension;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.StatCacheBuilder;
import com.easyinsight.calculations.StatCalculationCache;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.awt.*;

/**
 * User: jamesboe
 * Date: 11/11/14
 * Time: 11:52 AM
 */
public class AutoTextFromBackground extends Function {

    @Override
    public Value evaluate() {
        String statName = minusBrackets(getParameterName(0));
        AnalysisItem statMeasure = findDataSourceItem(0);

        // rtbportal
        if (statMeasure == null) {
            throw new FunctionException("Could not find the specified field " + statName);
        }


        Value target = getParameter(0);

        TextValueExtension textValueExtension = (TextValueExtension) target.getValueExtension();
        if (textValueExtension == null) {
            return new EmptyValue();
        }
        int color = textValueExtension.getBackgroundColor();
        Color c = new Color(color);
        if (c.getRed() > 150 && c.getGreen() > 150 && c.getBlue() > 150) {
            textValueExtension.setColor(0x000000);
        } else {
            textValueExtension.setColor(0xFFFFFF);
        }
        return new EmptyValue();
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    @Override
    public int getParameterCount() {
        return 1;
    }
}
