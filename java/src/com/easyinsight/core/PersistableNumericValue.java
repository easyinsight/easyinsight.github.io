package com.easyinsight.core;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * User: James Boe
 * Date: Jul 16, 2008
 * Time: 12:46:55 AM
 */

@Entity
@Table(name="numeric_value")
@PrimaryKeyJoinColumn(name="value_id")
public class PersistableNumericValue extends PersistableValue {
    @Column(name="numeric_content")
    private double numberValue;

    public PersistableNumericValue() {
    }

    public PersistableNumericValue(double numberValue) {
        this.numberValue = numberValue;
    }

    public double getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(double numberValue) {
        this.numberValue = numberValue;
    }

    public Value toValue() {
        return new NumericValue(numberValue);
    }
}
