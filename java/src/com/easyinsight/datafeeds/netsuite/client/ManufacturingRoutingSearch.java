
package com.easyinsight.datafeeds.netsuite.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManufacturingRoutingSearch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManufacturingRoutingSearch">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}SearchRecord">
 *       &lt;sequence>
 *         &lt;element name="basic" type="{urn:common_2014_1.platform.webservices.netsuite.com}ManufacturingRoutingSearchBasic" minOccurs="0"/>
 *         &lt;element name="itemJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}ItemSearchBasic" minOccurs="0"/>
 *         &lt;element name="locationJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}LocationSearchBasic" minOccurs="0"/>
 *         &lt;element name="manufacturingCostTemplateJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}ManufacturingCostTemplateSearchBasic" minOccurs="0"/>
 *         &lt;element name="manufacturingWorkCenterJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}EntityGroupSearchBasic" minOccurs="0"/>
 *         &lt;element name="userJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}EmployeeSearchBasic" minOccurs="0"/>
 *         &lt;element name="customSearchJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}CustomSearchJoin" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManufacturingRoutingSearch", namespace = "urn:supplychain_2014_1.lists.webservices.netsuite.com", propOrder = {
    "basic",
    "itemJoin",
    "locationJoin",
    "manufacturingCostTemplateJoin",
    "manufacturingWorkCenterJoin",
    "userJoin",
    "customSearchJoin"
})
public class ManufacturingRoutingSearch
    extends SearchRecord
{

    protected ManufacturingRoutingSearchBasic basic;
    protected ItemSearchBasic itemJoin;
    protected LocationSearchBasic locationJoin;
    protected ManufacturingCostTemplateSearchBasic manufacturingCostTemplateJoin;
    protected EntityGroupSearchBasic manufacturingWorkCenterJoin;
    protected EmployeeSearchBasic userJoin;
    protected List<CustomSearchJoin> customSearchJoin;

    /**
     * Gets the value of the basic property.
     * 
     * @return
     *     possible object is
     *     {@link ManufacturingRoutingSearchBasic }
     *     
     */
    public ManufacturingRoutingSearchBasic getBasic() {
        return basic;
    }

    /**
     * Sets the value of the basic property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManufacturingRoutingSearchBasic }
     *     
     */
    public void setBasic(ManufacturingRoutingSearchBasic value) {
        this.basic = value;
    }

    /**
     * Gets the value of the itemJoin property.
     * 
     * @return
     *     possible object is
     *     {@link ItemSearchBasic }
     *     
     */
    public ItemSearchBasic getItemJoin() {
        return itemJoin;
    }

    /**
     * Sets the value of the itemJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemSearchBasic }
     *     
     */
    public void setItemJoin(ItemSearchBasic value) {
        this.itemJoin = value;
    }

    /**
     * Gets the value of the locationJoin property.
     * 
     * @return
     *     possible object is
     *     {@link LocationSearchBasic }
     *     
     */
    public LocationSearchBasic getLocationJoin() {
        return locationJoin;
    }

    /**
     * Sets the value of the locationJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocationSearchBasic }
     *     
     */
    public void setLocationJoin(LocationSearchBasic value) {
        this.locationJoin = value;
    }

    /**
     * Gets the value of the manufacturingCostTemplateJoin property.
     * 
     * @return
     *     possible object is
     *     {@link ManufacturingCostTemplateSearchBasic }
     *     
     */
    public ManufacturingCostTemplateSearchBasic getManufacturingCostTemplateJoin() {
        return manufacturingCostTemplateJoin;
    }

    /**
     * Sets the value of the manufacturingCostTemplateJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManufacturingCostTemplateSearchBasic }
     *     
     */
    public void setManufacturingCostTemplateJoin(ManufacturingCostTemplateSearchBasic value) {
        this.manufacturingCostTemplateJoin = value;
    }

    /**
     * Gets the value of the manufacturingWorkCenterJoin property.
     * 
     * @return
     *     possible object is
     *     {@link EntityGroupSearchBasic }
     *     
     */
    public EntityGroupSearchBasic getManufacturingWorkCenterJoin() {
        return manufacturingWorkCenterJoin;
    }

    /**
     * Sets the value of the manufacturingWorkCenterJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityGroupSearchBasic }
     *     
     */
    public void setManufacturingWorkCenterJoin(EntityGroupSearchBasic value) {
        this.manufacturingWorkCenterJoin = value;
    }

    /**
     * Gets the value of the userJoin property.
     * 
     * @return
     *     possible object is
     *     {@link EmployeeSearchBasic }
     *     
     */
    public EmployeeSearchBasic getUserJoin() {
        return userJoin;
    }

    /**
     * Sets the value of the userJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmployeeSearchBasic }
     *     
     */
    public void setUserJoin(EmployeeSearchBasic value) {
        this.userJoin = value;
    }

    /**
     * Gets the value of the customSearchJoin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customSearchJoin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomSearchJoin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomSearchJoin }
     * 
     * 
     */
    public List<CustomSearchJoin> getCustomSearchJoin() {
        if (customSearchJoin == null) {
            customSearchJoin = new ArrayList<CustomSearchJoin>();
        }
        return this.customSearchJoin;
    }

}
