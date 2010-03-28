package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Mar 26, 2010
 * Time: 3:39:06 PM
 */
@Entity
@Table(name="analysis_zip")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisZipCode extends AnalysisDimension {
    @Override
    public int getType() {
        return super.getType() | AnalysisItemTypes.ZIP_CODE;
    }
}
