
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PayrollItemItemTypeNoHierarchy.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PayrollItemItemTypeNoHierarchy">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_addition"/>
 *     &lt;enumeration value="_commission"/>
 *     &lt;enumeration value="_deduction"/>
 *     &lt;enumeration value="_directDeposit"/>
 *     &lt;enumeration value="_disability"/>
 *     &lt;enumeration value="_earning"/>
 *     &lt;enumeration value="_employerContribution"/>
 *     &lt;enumeration value="_expense"/>
 *     &lt;enumeration value="_federal"/>
 *     &lt;enumeration value="_medicare"/>
 *     &lt;enumeration value="_other"/>
 *     &lt;enumeration value="_salary"/>
 *     &lt;enumeration value="_sick"/>
 *     &lt;enumeration value="_socialSecurity"/>
 *     &lt;enumeration value="_state"/>
 *     &lt;enumeration value="_tax"/>
 *     &lt;enumeration value="_unemployment"/>
 *     &lt;enumeration value="_vacation"/>
 *     &lt;enumeration value="_wage"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PayrollItemItemTypeNoHierarchy", namespace = "urn:types.employees_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum PayrollItemItemTypeNoHierarchy {

    @XmlEnumValue("_addition")
    ADDITION("_addition"),
    @XmlEnumValue("_commission")
    COMMISSION("_commission"),
    @XmlEnumValue("_deduction")
    DEDUCTION("_deduction"),
    @XmlEnumValue("_directDeposit")
    DIRECT_DEPOSIT("_directDeposit"),
    @XmlEnumValue("_disability")
    DISABILITY("_disability"),
    @XmlEnumValue("_earning")
    EARNING("_earning"),
    @XmlEnumValue("_employerContribution")
    EMPLOYER_CONTRIBUTION("_employerContribution"),
    @XmlEnumValue("_expense")
    EXPENSE("_expense"),
    @XmlEnumValue("_federal")
    FEDERAL("_federal"),
    @XmlEnumValue("_medicare")
    MEDICARE("_medicare"),
    @XmlEnumValue("_other")
    OTHER("_other"),
    @XmlEnumValue("_salary")
    SALARY("_salary"),
    @XmlEnumValue("_sick")
    SICK("_sick"),
    @XmlEnumValue("_socialSecurity")
    SOCIAL_SECURITY("_socialSecurity"),
    @XmlEnumValue("_state")
    STATE("_state"),
    @XmlEnumValue("_tax")
    TAX("_tax"),
    @XmlEnumValue("_unemployment")
    UNEMPLOYMENT("_unemployment"),
    @XmlEnumValue("_vacation")
    VACATION("_vacation"),
    @XmlEnumValue("_wage")
    WAGE("_wage");
    private final String value;

    PayrollItemItemTypeNoHierarchy(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PayrollItemItemTypeNoHierarchy fromValue(String v) {
        for (PayrollItemItemTypeNoHierarchy c: PayrollItemItemTypeNoHierarchy.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
