package com.easyinsight.analysis;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 7, 2008
 * Time: 11:23:23 AM
 */
@Entity
@Table(name="formatting_configuration")
public class FormattingConfiguration implements Serializable, Cloneable {

    public static final int NUMBER = 1;
    public static final int CURRENCY = 2;
    public static final int PERCENTAGE = 3;
    public static final int TEXT_UOM = 4;

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="formatting_configuration_id")
    private long formattingConfigurationID;
    @Column(name="formatting_type")
    private int formattingType;
    @Column(name="text_uom")
    private String textUom;

    public FormattingConfiguration clone() throws CloneNotSupportedException {
        FormattingConfiguration formattingConfiguration = (FormattingConfiguration) super.clone();
        formattingConfiguration.setFormattingConfigurationID(0);
        return formattingConfiguration;
    }

    public long getFormattingConfigurationID() {
        return formattingConfigurationID;
    }

    public void setFormattingConfigurationID(long formattingConfigurationID) {
        this.formattingConfigurationID = formattingConfigurationID;
    }

    public int getFormattingType() {
        return formattingType;
    }

    public void setFormattingType(int formattingType) {
        this.formattingType = formattingType;
    }

    public String getTextUom() {
        return textUom;
    }

    public void setTextUom(String textUom) {
        this.textUom = textUom;
    }
}
