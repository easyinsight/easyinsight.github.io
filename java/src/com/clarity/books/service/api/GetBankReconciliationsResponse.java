
package com.clarity.books.service.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getBankReconciliationsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getBankReconciliationsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bankReconciliation" type="{http://api.service.books/}bankReconciliationInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBankReconciliationsResponse", propOrder = {
    "bankReconciliation"
})
public class GetBankReconciliationsResponse {

    @XmlElement(nillable = true)
    protected List<BankReconciliationInfo> bankReconciliation;

    /**
     * Gets the value of the bankReconciliation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bankReconciliation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBankReconciliation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BankReconciliationInfo }
     * 
     * 
     */
    public List<BankReconciliationInfo> getBankReconciliation() {
        if (bankReconciliation == null) {
            bankReconciliation = new ArrayList<BankReconciliationInfo>();
        }
        return this.bankReconciliation;
    }

}
