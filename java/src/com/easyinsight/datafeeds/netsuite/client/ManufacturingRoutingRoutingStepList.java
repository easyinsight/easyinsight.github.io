
package com.easyinsight.datafeeds.netsuite.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManufacturingRoutingRoutingStepList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManufacturingRoutingRoutingStepList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="manufacturingRoutingRoutingStep" type="{urn:supplychain_2014_1.lists.webservices.netsuite.com}ManufacturingRoutingRoutingStep" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="replaceAll" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManufacturingRoutingRoutingStepList", namespace = "urn:supplychain_2014_1.lists.webservices.netsuite.com", propOrder = {
    "manufacturingRoutingRoutingStep"
})
public class ManufacturingRoutingRoutingStepList {

    protected List<ManufacturingRoutingRoutingStep> manufacturingRoutingRoutingStep;
    @XmlAttribute
    protected Boolean replaceAll;

    /**
     * Gets the value of the manufacturingRoutingRoutingStep property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the manufacturingRoutingRoutingStep property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getManufacturingRoutingRoutingStep().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ManufacturingRoutingRoutingStep }
     * 
     * 
     */
    public List<ManufacturingRoutingRoutingStep> getManufacturingRoutingRoutingStep() {
        if (manufacturingRoutingRoutingStep == null) {
            manufacturingRoutingRoutingStep = new ArrayList<ManufacturingRoutingRoutingStep>();
        }
        return this.manufacturingRoutingRoutingStep;
    }

    /**
     * Gets the value of the replaceAll property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isReplaceAll() {
        if (replaceAll == null) {
            return true;
        } else {
            return replaceAll;
        }
    }

    /**
     * Sets the value of the replaceAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReplaceAll(Boolean value) {
        this.replaceAll = value;
    }

}
