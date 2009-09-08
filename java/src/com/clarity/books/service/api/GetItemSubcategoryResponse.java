
package com.clarity.books.service.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getItemSubcategoryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getItemSubcategoryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="category" type="{http://api.service.books/}itemCategoryInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getItemSubcategoryResponse", propOrder = {
    "category"
})
public class GetItemSubcategoryResponse {

    protected ItemCategoryInfo category;

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link ItemCategoryInfo }
     *     
     */
    public ItemCategoryInfo getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemCategoryInfo }
     *     
     */
    public void setCategory(ItemCategoryInfo value) {
        this.category = value;
    }

}
