
package com.easyinsight.datafeeds.netsuite.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SearchEnumMultiSelectCustomField complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchEnumMultiSelectCustomField">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}SearchCustomField">
 *       &lt;sequence>
 *         &lt;element name="searchValue" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="operator" type="{urn:types.core_2014_1.platform.webservices.netsuite.com}SearchEnumMultiSelectFieldOperator" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchEnumMultiSelectCustomField", namespace = "urn:core_2014_1.platform.webservices.netsuite.com", propOrder = {
    "searchValue"
})
public class SearchEnumMultiSelectCustomField
    extends SearchCustomField
{

    protected List<String> searchValue;
    @XmlAttribute
    protected SearchEnumMultiSelectFieldOperator operator;

    /**
     * Gets the value of the searchValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSearchValue() {
        if (searchValue == null) {
            searchValue = new ArrayList<String>();
        }
        return this.searchValue;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link SearchEnumMultiSelectFieldOperator }
     *     
     */
    public SearchEnumMultiSelectFieldOperator getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchEnumMultiSelectFieldOperator }
     *     
     */
    public void setOperator(SearchEnumMultiSelectFieldOperator value) {
        this.operator = value;
    }

}
