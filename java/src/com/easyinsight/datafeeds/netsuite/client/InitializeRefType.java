
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InitializeRefType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InitializeRefType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="assemblyItem"/>
 *     &lt;enumeration value="assemblyBuild"/>
 *     &lt;enumeration value="cashSale"/>
 *     &lt;enumeration value="creditMemo"/>
 *     &lt;enumeration value="customer"/>
 *     &lt;enumeration value="customerDeposit"/>
 *     &lt;enumeration value="employee"/>
 *     &lt;enumeration value="estimate"/>
 *     &lt;enumeration value="interCompanyTransferOrder"/>
 *     &lt;enumeration value="invoice"/>
 *     &lt;enumeration value="location"/>
 *     &lt;enumeration value="lotNumberedAssemblyItem"/>
 *     &lt;enumeration value="opportunity"/>
 *     &lt;enumeration value="purchaseOrder"/>
 *     &lt;enumeration value="returnAuthorization"/>
 *     &lt;enumeration value="salesOrder"/>
 *     &lt;enumeration value="serializedAssemblyItem"/>
 *     &lt;enumeration value="transferOrder"/>
 *     &lt;enumeration value="vendor"/>
 *     &lt;enumeration value="vendorBill"/>
 *     &lt;enumeration value="vendorReturnAuthorization"/>
 *     &lt;enumeration value="workOrder"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InitializeRefType", namespace = "urn:types.core_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum InitializeRefType {

    @XmlEnumValue("assemblyItem")
    ASSEMBLY_ITEM("assemblyItem"),
    @XmlEnumValue("assemblyBuild")
    ASSEMBLY_BUILD("assemblyBuild"),
    @XmlEnumValue("cashSale")
    CASH_SALE("cashSale"),
    @XmlEnumValue("creditMemo")
    CREDIT_MEMO("creditMemo"),
    @XmlEnumValue("customer")
    CUSTOMER("customer"),
    @XmlEnumValue("customerDeposit")
    CUSTOMER_DEPOSIT("customerDeposit"),
    @XmlEnumValue("employee")
    EMPLOYEE("employee"),
    @XmlEnumValue("estimate")
    ESTIMATE("estimate"),
    @XmlEnumValue("interCompanyTransferOrder")
    INTER_COMPANY_TRANSFER_ORDER("interCompanyTransferOrder"),
    @XmlEnumValue("invoice")
    INVOICE("invoice"),
    @XmlEnumValue("location")
    LOCATION("location"),
    @XmlEnumValue("lotNumberedAssemblyItem")
    LOT_NUMBERED_ASSEMBLY_ITEM("lotNumberedAssemblyItem"),
    @XmlEnumValue("opportunity")
    OPPORTUNITY("opportunity"),
    @XmlEnumValue("purchaseOrder")
    PURCHASE_ORDER("purchaseOrder"),
    @XmlEnumValue("returnAuthorization")
    RETURN_AUTHORIZATION("returnAuthorization"),
    @XmlEnumValue("salesOrder")
    SALES_ORDER("salesOrder"),
    @XmlEnumValue("serializedAssemblyItem")
    SERIALIZED_ASSEMBLY_ITEM("serializedAssemblyItem"),
    @XmlEnumValue("transferOrder")
    TRANSFER_ORDER("transferOrder"),
    @XmlEnumValue("vendor")
    VENDOR("vendor"),
    @XmlEnumValue("vendorBill")
    VENDOR_BILL("vendorBill"),
    @XmlEnumValue("vendorReturnAuthorization")
    VENDOR_RETURN_AUTHORIZATION("vendorReturnAuthorization"),
    @XmlEnumValue("workOrder")
    WORK_ORDER("workOrder");
    private final String value;

    InitializeRefType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InitializeRefType fromValue(String v) {
        for (InitializeRefType c: InitializeRefType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
