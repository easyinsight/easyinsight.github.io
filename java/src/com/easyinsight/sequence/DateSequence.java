package com.easyinsight.sequence;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 10:37:27 AM
 */
@Entity
@Table(name="date_sequence")
@PrimaryKeyJoinColumn(name="report_sequence_id")
public class DateSequence extends Sequence {

    public static final int DAY = 1;
    public static final int WEEK = 2;
    public static final int MONTH = 3;
    public static final int YEAR = 4;

    @Column(name="date_type")
    private int dateType = DAY;

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    @Override
    public AnalysisItem toAnalysisItem() {
        AnalysisDateDimension analysisDateDimension = (AnalysisDateDimension) getAnalysisItem();
        /*if (dateType == DAY) {
            analysisDateDimension.setDateLevel(AnalysisDateDimension.DAY_LEVEL);
        } else if (dateType == WEEK) {
            analysisDateDimension.setDateLevel(AnalysisDateDimension.WEEK_LEVEL);
        } else if (dateType == MONTH) {
            analysisDateDimension.setDateLevel(AnalysisDateDimension.MONTH_LEVEL);
        } else if (dateType == YEAR) {
            analysisDateDimension.setDateLevel(AnalysisDateDimension.YEAR_LEVEL);
        }*/
        analysisDateDimension.setDateLevel(AnalysisDateDimension.WEEK_LEVEL);
        return analysisDateDimension;
    }
}
