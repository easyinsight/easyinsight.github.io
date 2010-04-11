package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;

import javax.persistence.*;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

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

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="analysis_zip_id")
    private AnalysisZipCode analysisZipCode;

    public AnalysisZipCode getAnalysisZipCode() {
        return analysisZipCode;
    }

    public void setAnalysisZipCode(AnalysisZipCode analysisZipCode) {
        this.analysisZipCode = analysisZipCode;
    }

    @Override
    public boolean isDerived() {
        return analysisZipCode != null;
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (analysisZipCode != null) {
            setAnalysisZipCode((AnalysisZipCode) Database.deproxy(getAnalysisZipCode()));
            analysisZipCode.afterLoad();
        }
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

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters) {
        List<AnalysisItem> items = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters);
        if (analysisZipCode != null) {
            items.add(analysisZipCode);
        }
        return items;
    }
}
