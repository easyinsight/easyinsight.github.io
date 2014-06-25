package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.ZipGeocodeCache;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 6/24/14
 * Time: 9:09 AM
 */
public class LatitudeFromPostal extends Function {
    @Override
    public Value evaluate() {
        Double dValue = new ZipGeocodeCache().findLatitudeForZip(params.get(0).toString(), calculationMetadata.getConnection());
        if (dValue == null) {
            return new EmptyValue();
        }
        return new NumericValue(dValue);
    }

    @Override
    public int getParameterCount() {
        return 1;
    }
}
