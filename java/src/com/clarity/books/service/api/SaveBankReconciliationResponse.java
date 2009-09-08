
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveBankReconciliationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveBankReconciliationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bankReconciliation" type="{http://api.service.books/}bankReconciliationInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveBankReconciliationResponse", propOrder = {
    "bankReconciliation"
})
public class SaveBankReconciliationResponse {

    protected BankReconciliationInfo bankReconciliation;

    /**
     * Gets the value of the bankReconciliation property.
     * 
     * @return
     *     possible object is
     *     {@link BankReconciliationInfo }
     *     
     */
    public BankReconciliationInfo getBankReconciliation() {
        return bankReconciliation;
    }

    /**
     * Sets the value of the bankReconciliation property.
     * 
     * @param value
     *     allowed object is
     *     {@link BankReconciliationInfo }
     *     
     */
    public void setBankReconciliation(BankReconciliationInfo value) {
        this.bankReconciliation = value;
    }

}
