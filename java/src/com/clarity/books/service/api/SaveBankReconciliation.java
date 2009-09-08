
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for saveBankReconciliation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveBankReconciliation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="token" type="{http://api.service.books/}authToken" minOccurs="0"/>
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
@XmlType(name = "saveBankReconciliation", propOrder = {
    "token",
    "bankReconciliation"
})
public class SaveBankReconciliation {

    protected AuthToken token;
    protected BankReconciliationInfo bankReconciliation;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link AuthToken }
     *     
     */
    public AuthToken getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthToken }
     *     
     */
    public void setToken(AuthToken value) {
        this.token = value;
    }

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
