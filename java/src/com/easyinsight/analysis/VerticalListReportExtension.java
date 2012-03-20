package com.easyinsight.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * User: jamesboe
 * Date: 10/25/11
 * Time: 11:47 AM
 */
@Entity
@Table(name="vertical_list_field_extension")
public class VerticalListReportExtension extends ReportFieldExtension {
    @Column(name="line_above")
    private boolean lineAbove;

    public boolean isLineAbove() {
        return lineAbove;
    }

    public void setLineAbove(boolean lineAbove) {
        this.lineAbove = lineAbove;
    }
}
