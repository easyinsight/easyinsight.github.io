package com.easyinsight.core;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.util.Date;

/**
 * User: James Boe
 * Date: Jul 16, 2008
 * Time: 12:46:40 AM
 */

@Entity
@Table(name="date_value")
@PrimaryKeyJoinColumn(name="value_id")
public class PersistableDateValue extends PersistableValue {
    @Column (name="date_contet")
    private Date dateValue;

    public PersistableDateValue() {
    }

    public PersistableDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Value toValue() {
        return new DateValue(dateValue);
    }
}
