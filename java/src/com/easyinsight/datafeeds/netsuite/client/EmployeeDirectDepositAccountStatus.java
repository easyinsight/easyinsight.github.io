
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EmployeeDirectDepositAccountStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EmployeeDirectDepositAccountStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_active"/>
 *     &lt;enumeration value="_pending"/>
 *     &lt;enumeration value="_verifying"/>
 *     &lt;enumeration value="_failed"/>
 *     &lt;enumeration value="_inactive"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EmployeeDirectDepositAccountStatus", namespace = "urn:types.employees_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum EmployeeDirectDepositAccountStatus {

    @XmlEnumValue("_active")
    ACTIVE("_active"),
    @XmlEnumValue("_pending")
    PENDING("_pending"),
    @XmlEnumValue("_verifying")
    VERIFYING("_verifying"),
    @XmlEnumValue("_failed")
    FAILED("_failed"),
    @XmlEnumValue("_inactive")
    INACTIVE("_inactive");
    private final String value;

    EmployeeDirectDepositAccountStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EmployeeDirectDepositAccountStatus fromValue(String v) {
        for (EmployeeDirectDepositAccountStatus c: EmployeeDirectDepositAccountStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
