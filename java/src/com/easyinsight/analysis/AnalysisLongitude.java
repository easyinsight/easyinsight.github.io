package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 2:07:38 PM
 */
@Entity
@Table(name="analysis_longitude")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisLongitude extends AnalysisCoordinate {

    public AnalysisLongitude(Key key, boolean group, String displayName, int precision) {
        super(key, group, displayName, precision);
    }

    public AnalysisLongitude() {
    }

    @Override
    public int getType() {
        return super.getType() | AnalysisItemTypes.LONGITUDE;
    }
}
