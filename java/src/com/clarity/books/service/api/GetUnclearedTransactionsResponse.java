
package com.clarity.books.service.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getUnclearedTransactionsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getUnclearedTransactionsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="journalEntry" type="{http://api.service.books/}journalEntryInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUnclearedTransactionsResponse", propOrder = {
    "journalEntry"
})
public class GetUnclearedTransactionsResponse {

    @XmlElement(nillable = true)
    protected List<JournalEntryInfo> journalEntry;

    /**
     * Gets the value of the journalEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the journalEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJournalEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JournalEntryInfo }
     * 
     * 
     */
    public List<JournalEntryInfo> getJournalEntry() {
        if (journalEntry == null) {
            journalEntry = new ArrayList<JournalEntryInfo>();
        }
        return this.journalEntry;
    }

}
