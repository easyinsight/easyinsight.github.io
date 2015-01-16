
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetAllRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAllRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="record" type="{urn:core_2014_1.platform.webservices.netsuite.com}GetAllRecord"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAllRequest", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com", propOrder = {
    "record"
})
public class GetAllRequest {

    @XmlElement(required = true)
    protected GetAllRecord record;

    /**
     * Gets the value of the record property.
     * 
     * @return
     *     possible object is
     *     {@link GetAllRecord }
     *     
     */
    public GetAllRecord getRecord() {
        return record;
    }

    /**
     * Sets the value of the record property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetAllRecord }
     *     
     */
    public void setRecord(GetAllRecord value) {
        this.record = value;
    }

}
