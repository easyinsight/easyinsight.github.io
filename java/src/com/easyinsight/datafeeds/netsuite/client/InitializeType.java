
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InitializeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InitializeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="assemblyBuild"/>
 *     &lt;enumeration value="assemblyUnbuild"/>
 *     &lt;enumeration value="binWorksheet"/>
 *     &lt;enumeration value="cashRefund"/>
 *     &lt;enumeration value="cashSale"/>
 *     &lt;enumeration value="creditMemo"/>
 *     &lt;enumeration value="customerPayment"/>
 *     &lt;enumeration value="customerRefund"/>
 *     &lt;enumeration value="depositApplication"/>
 *     &lt;enumeration value="estimate"/>
 *     &lt;enumeration value="invoice"/>
 *     &lt;enumeration value="itemFulfillment"/>
 *     &lt;enumeration value="itemReceipt"/>
 *     &lt;enumeration value="inventoryTransfer"/>
 *     &lt;enumeration value="returnAuthorization"/>
 *     &lt;enumeration value="salesOrder"/>
 *     &lt;enumeration value="vendorBill"/>
 *     &lt;enumeration value="vendorCredit"/>
 *     &lt;enumeration value="vendorReturnAuthorization"/>
 *     &lt;enumeration value="vendorPayment"/>
 *     &lt;enumeration value="workOrder"/>
 *     &lt;enumeration value="workOrderIssue"/>
 *     &lt;enumeration value="workOrderCompletion"/>
 *     &lt;enumeration value="workOrderClose"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InitializeType", namespace = "urn:types.core_2014_1.platform.webservices.netsuite.com")
@XmlEnum
public enum InitializeType {

    @XmlEnumValue("assemblyBuild")
    ASSEMBLY_BUILD("assemblyBuild"),
    @XmlEnumValue("assemblyUnbuild")
    ASSEMBLY_UNBUILD("assemblyUnbuild"),
    @XmlEnumValue("binWorksheet")
    BIN_WORKSHEET("binWorksheet"),
    @XmlEnumValue("cashRefund")
    CASH_REFUND("cashRefund"),
    @XmlEnumValue("cashSale")
    CASH_SALE("cashSale"),
    @XmlEnumValue("creditMemo")
    CREDIT_MEMO("creditMemo"),
    @XmlEnumValue("customerPayment")
    CUSTOMER_PAYMENT("customerPayment"),
    @XmlEnumValue("customerRefund")
    CUSTOMER_REFUND("customerRefund"),
    @XmlEnumValue("depositApplication")
    DEPOSIT_APPLICATION("depositApplication"),
    @XmlEnumValue("estimate")
    ESTIMATE("estimate"),
    @XmlEnumValue("invoice")
    INVOICE("invoice"),
    @XmlEnumValue("itemFulfillment")
    ITEM_FULFILLMENT("itemFulfillment"),
    @XmlEnumValue("itemReceipt")
    ITEM_RECEIPT("itemReceipt"),
    @XmlEnumValue("inventoryTransfer")
    INVENTORY_TRANSFER("inventoryTransfer"),
    @XmlEnumValue("returnAuthorization")
    RETURN_AUTHORIZATION("returnAuthorization"),
    @XmlEnumValue("salesOrder")
    SALES_ORDER("salesOrder"),
    @XmlEnumValue("vendorBill")
    VENDOR_BILL("vendorBill"),
    @XmlEnumValue("vendorCredit")
    VENDOR_CREDIT("vendorCredit"),
    @XmlEnumValue("vendorReturnAuthorization")
    VENDOR_RETURN_AUTHORIZATION("vendorReturnAuthorization"),
    @XmlEnumValue("vendorPayment")
    VENDOR_PAYMENT("vendorPayment"),
    @XmlEnumValue("workOrder")
    WORK_ORDER("workOrder"),
    @XmlEnumValue("workOrderIssue")
    WORK_ORDER_ISSUE("workOrderIssue"),
    @XmlEnumValue("workOrderCompletion")
    WORK_ORDER_COMPLETION("workOrderCompletion"),
    @XmlEnumValue("workOrderClose")
    WORK_ORDER_CLOSE("workOrderClose");
    private final String value;

    InitializeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InitializeType fromValue(String v) {
        for (InitializeType c: InitializeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
