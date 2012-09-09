package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import nu.xom.Attribute;
import nu.xom.Element;

import javax.persistence.*;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;

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
    public static final int MILLISECONDS = 4;
    public static final int SECONDS = 5;
    public static final int BYTES = 6;    

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

    public NumberFormat createFormatter() {
        NumberFormat nf = null;

        switch(formattingType) {
            case NUMBER:
                nf = new DecimalFormat();
                nf.setMaximumFractionDigits(2);
                break;
            case CURRENCY:
                nf = NumberFormat.getCurrencyInstance();
                break;
            case PERCENTAGE:
                nf = new PercentNumberFormat();
                break;
            default:
                nf = new DecimalFormat();
                nf.setMaximumFractionDigits(2);
        }
        return nf;
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

    public Element toXML() {
        Element element = new Element("formattingConfiguration");
        element.addAttribute(new Attribute("formattingType", String.valueOf(formattingType)));
        return element;
    }

    public static FormattingConfiguration fromXML(Element fieldNode) {
        Element formatting = (Element) fieldNode.query("formattingConfiguration").get(0);
        FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
        formattingConfiguration.setFormattingType(Integer.parseInt(formatting.getAttribute("formattingType").getValue()));
        return formattingConfiguration;
    }

    class PercentNumberFormat extends NumberFormat {

        private NumberFormat format = NumberFormat.getPercentInstance();

        public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
            return format.format(number/100.0, toAppendTo, pos);
        }

        public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
            return format.format(number/100.0, toAppendTo, pos);
        }

        public Number parse(String source, ParsePosition parsePosition) {
            return format.parse(source, parsePosition).doubleValue() * 100.0;
        }
    }
}
