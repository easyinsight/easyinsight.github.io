
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AppDefinitionSearch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AppDefinitionSearch">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}SearchRecord">
 *       &lt;sequence>
 *         &lt;element name="basic" type="{urn:common_2014_1.platform.webservices.netsuite.com}AppDefinitionSearchBasic" minOccurs="0"/>
 *         &lt;element name="appPackageJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}AppPackageSearchBasic" minOccurs="0"/>
 *         &lt;element name="creatorJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}EmployeeSearchBasic" minOccurs="0"/>
 *         &lt;element name="userJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}EmployeeSearchBasic" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppDefinitionSearch", namespace = "urn:customization_2014_1.setup.webservices.netsuite.com", propOrder = {
    "basic",
    "appPackageJoin",
    "creatorJoin",
    "userJoin"
})
public class AppDefinitionSearch
    extends SearchRecord
{

    protected AppDefinitionSearchBasic basic;
    protected AppPackageSearchBasic appPackageJoin;
    protected EmployeeSearchBasic creatorJoin;
    protected EmployeeSearchBasic userJoin;

    /**
     * Gets the value of the basic property.
     * 
     * @return
     *     possible object is
     *     {@link AppDefinitionSearchBasic }
     *     
     */
    public AppDefinitionSearchBasic getBasic() {
        return basic;
    }

    /**
     * Sets the value of the basic property.
     * 
     * @param value
     *     allowed object is
     *     {@link AppDefinitionSearchBasic }
     *     
     */
    public void setBasic(AppDefinitionSearchBasic value) {
        this.basic = value;
    }

    /**
     * Gets the value of the appPackageJoin property.
     * 
     * @return
     *     possible object is
     *     {@link AppPackageSearchBasic }
     *     
     */
    public AppPackageSearchBasic getAppPackageJoin() {
        return appPackageJoin;
    }

    /**
     * Sets the value of the appPackageJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link AppPackageSearchBasic }
     *     
     */
    public void setAppPackageJoin(AppPackageSearchBasic value) {
        this.appPackageJoin = value;
    }

    /**
     * Gets the value of the creatorJoin property.
     * 
     * @return
     *     possible object is
     *     {@link EmployeeSearchBasic }
     *     
     */
    public EmployeeSearchBasic getCreatorJoin() {
        return creatorJoin;
    }

    /**
     * Sets the value of the creatorJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmployeeSearchBasic }
     *     
     */
    public void setCreatorJoin(EmployeeSearchBasic value) {
        this.creatorJoin = value;
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

}
