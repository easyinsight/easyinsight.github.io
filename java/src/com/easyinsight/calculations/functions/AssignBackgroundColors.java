package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.TextValueExtension;
import com.easyinsight.calculations.ColorCache;
import com.easyinsight.calculations.ColorCacheBuilder;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;
import com.easyinsight.logging.LogClass;

/**
 * User: jamesboe
 * Date: 7/21/14
 * Time: 9:50 AM
 */
public class AssignBackgroundColors extends Function {
    @Override
    public Value evaluate() {
        try {
            AnalysisItem colorColumn = findDataSourceItem(0);
            Value target;
            Value colorSource = getParameter(0);
            if (getParameterCount() == 2) {
                target = getParameter(1);
            } else {
                target = colorSource;
            }

            String processName = "colors";
            ColorCache colorCache = (ColorCache) calculationMetadata.getCache(new ColorCacheBuilder(colorColumn, calculationMetadata), processName);
            TextValueExtension textValueExtension = (TextValueExtension) target.getValueExtension();
            if (textValueExtension == null) {
                textValueExtension = new TextValueExtension();
                target.setValueExtension(textValueExtension);
            }
            textValueExtension.setColor(0xFFFFFF);
            textValueExtension.setBackgroundColor(colorCache.colorForValue(colorSource));
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    @Override
    public int getParameterCount() {
        return -1;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
