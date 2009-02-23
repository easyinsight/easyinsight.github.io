package com.easyinsight.analysis;

import com.easyinsight.core.*;
import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.analysis.AnalysisDateDimensionResultMetadata;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;

/**
 * User: James Boe
 * Date: Feb 29, 2008
 * Time: 10:49:36 AM
 */
@Entity
@Table(name="analysis_date")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisDateDimension extends AnalysisDimension {
    @Column(name="date_level")
    private int dateLevel;
    @Column(name="custom_date_format")
    private String customDateFormat;
    private transient DateFormat cachedDateFormat;
    private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public AnalysisDateDimension(Key key, boolean group, int dateLevel) {
        super(key, group);
        this.dateLevel = dateLevel;
    }

    public AnalysisDateDimension(Key key, boolean group, int dateLevel, String customDateFormat) {
        super(key, group);
        this.dateLevel = dateLevel;
        this.customDateFormat = customDateFormat;
    }

    public AnalysisDateDimension(String key, boolean group, int dateLevel) {
        super(key, group);
        this.dateLevel = dateLevel;
    }

    public AnalysisDateDimension() {
    }

    public String getCustomDateFormat() {
        return customDateFormat;
    }

    public void setCustomDateFormat(String customDateFormat) {
        this.customDateFormat = customDateFormat;
    }

    public int getDateLevel() {
        return dateLevel;
    }

    public void setDateLevel(int dateLevel) {
        this.dateLevel = dateLevel;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.DATE_DIMENSION;
    }

    public Value renameMeLater(Value value) {
        if (cachedDateFormat == null) {
            if (customDateFormat == null) {
                cachedDateFormat = defaultDateFormat;
            } else {
                cachedDateFormat = new SimpleDateFormat(customDateFormat);
            }
        }
        Date tempDate = null;
        try {
            if (value.type() == Value.STRING) {
                StringValue stringValue = (StringValue) value;
                String rawString = stringValue.getValue();
                tempDate = cachedDateFormat.parse(rawString);
            } else if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                tempDate = dateValue.getDate();
            }
        } catch (ParseException e) {
        }
        if (tempDate != null)
            return new DateValue(tempDate);
        else
            return new EmptyValue();
    }

    public Value transformValue(Value value) {
        if (cachedDateFormat == null) {
            if (customDateFormat == null) {
                cachedDateFormat = defaultDateFormat;
            } else {
                cachedDateFormat = new SimpleDateFormat(customDateFormat);
            }
        }
        Date tempDate = null;
        Date finalDate = null;
        try {
            if (value.type() == Value.STRING) {
                StringValue stringValue = (StringValue) value;
                String rawString = stringValue.getValue();
                tempDate = cachedDateFormat.parse(rawString);
            } else if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                tempDate = dateValue.getDate();
            }
        } catch (ParseException e) {
        }
        if (tempDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempDate);
            switch (dateLevel) {
                case AnalysisItemTypes.YEAR_LEVEL:
                    calendar.set(Calendar.DAY_OF_YEAR, 1);
                    break;
                case AnalysisItemTypes.MONTH_LEVEL:
                    calendar.set(Calendar.DAY_OF_MONTH, 0);
                    break;
                case AnalysisItemTypes.DAY_LEVEL:
                    break;
                default:
            }
            finalDate = calendar.getTime();

            return new DateValue(finalDate);
        } else {
            return new EmptyValue();
        }
    }

    public AnalysisItemResultMetadata createResultMetadata() {
        return new AnalysisDateDimensionResultMetadata();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisDateDimension)) return false;
        if (!super.equals(o)) return false;

        AnalysisDateDimension that = (AnalysisDateDimension) o;

        return dateLevel == that.dateLevel;

    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + dateLevel;
        return result;
    }
}
