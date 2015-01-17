
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DetachBasicReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DetachBasicReference">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}DetachReference">
 *       &lt;sequence>
 *         &lt;element name="detachedRecord" type="{urn:core_2014_1.platform.webservices.netsuite.com}BaseRef"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DetachBasicReference", namespace = "urn:core_2014_1.platform.webservices.netsuite.com", propOrder = {
    "detachedRecord"
})
public class DetachBasicReference
    extends DetachReference
{

    @XmlElement(required = true)
    protected BaseRef detachedRecord;

    /**
     * Gets the value of the detachedRecord property.
     * 
     * @return
     *     possible object is
     *     {@link BaseRef }
     *     
     */
    public BaseRef getDetachedRecord() {
        return detachedRecord;
    }

    /**
     * Sets the value of the detachedRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseRef }
     *     
     */
    public void setDetachedRecord(BaseRef value) {
        this.detachedRecord = value;
    }

}
