package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 2:07:21 PM
 */
@Entity
@Table(name="analysis_latitude")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisLatitude extends AnalysisCoordinate {

    public AnalysisLatitude(Key key, boolean group, String displayName) {
        super(key, group, displayName);
    }

    public AnalysisLatitude() {
    }

    @Override
    public int getType() {
        return super.getType() | AnalysisItemTypes.LATITUDE;
    }
}
