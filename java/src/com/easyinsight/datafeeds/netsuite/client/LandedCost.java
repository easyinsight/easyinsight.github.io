
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LandedCost complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LandedCost">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}Record">
 *       &lt;sequence>
 *         &lt;element name="landedCostDataList" type="{urn:common_2014_1.platform.webservices.netsuite.com}LandedCostDataList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LandedCost", namespace = "urn:common_2014_1.platform.webservices.netsuite.com", propOrder = {
    "landedCostDataList"
})
public class LandedCost
    extends Record
{

    protected LandedCostDataList landedCostDataList;

    /**
     * Gets the value of the landedCostDataList property.
     * 
     * @return
     *     possible object is
     *     {@link LandedCostDataList }
     *     
     */
    public LandedCostDataList getLandedCostDataList() {
        return landedCostDataList;
    }

    /**
     * Sets the value of the landedCostDataList property.
     * 
     * @param value
     *     allowed object is
     *     {@link LandedCostDataList }
     *     
     */
    public void setLandedCostDataList(LandedCostDataList value) {
        this.landedCostDataList = value;
    }

}
