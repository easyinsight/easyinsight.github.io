package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.text.NumberFormat;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 2:07:31 PM
 */
@Entity
@Table(name="analysis_coordinate")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public abstract class AnalysisCoordinate extends AnalysisDimension {

    public AnalysisCoordinate(Key key, boolean group, String displayName, int precision) {
        super(key, displayName);
        this.precision = precision;
        setGroup(group);
    }

    public AnalysisCoordinate() {
    }

    @Column(name="precision_value")
    private int precision = 3;

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata) {
        Value result;
        double resultValue;
        try {
            if (value.type() == Value.STRING) {
                StringValue stringValue = (StringValue) value;
                resultValue = Double.parseDouble(stringValue.getValue());
            } else if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                resultValue = numericValue.toDouble();
            } else {
                resultValue = 0;
            }
        } catch (NumberFormatException e) {
            resultValue = 0;
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        String resultString = nf.format(resultValue);
        result = new StringValue(resultString);
        return result;
    }
}
