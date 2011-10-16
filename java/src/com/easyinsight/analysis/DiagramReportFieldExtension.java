package com.easyinsight.analysis;


import javax.persistence.*;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 10:02 AM
 */
@Entity
@Table(name="diagram_report_field_extension")
@PrimaryKeyJoinColumn(name="report_field_extension_id")
public class DiagramReportFieldExtension extends TrendReportFieldExtension {

    @Column(name="x")
    private int x;
    @Column(name="y")
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
