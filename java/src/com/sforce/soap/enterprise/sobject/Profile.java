
package com.sforce.soap.enterprise.sobject;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.sforce.soap.enterprise.QueryResult;


/**
 * <p>Java class for Profile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Profile">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:sobject.enterprise.soap.sforce.com}sObject">
 *       &lt;sequence>
 *         &lt;element name="CreatedBy" type="{urn:sobject.enterprise.soap.sforce.com}User" minOccurs="0"/>
 *         &lt;element name="CreatedById" type="{urn:enterprise.soap.sforce.com}ID" minOccurs="0"/>
 *         &lt;element name="CreatedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LastModifiedBy" type="{urn:sobject.enterprise.soap.sforce.com}User" minOccurs="0"/>
 *         &lt;element name="LastModifiedById" type="{urn:enterprise.soap.sforce.com}ID" minOccurs="0"/>
 *         &lt;element name="LastModifiedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="LicenseType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PermissionsApiUserOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsAuthorApex" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsConvertLeads" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsCreateMultiforce" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsCustomizeApplication" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsDisableNotifications" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsEditActivatedOrders" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsEditEvent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsEditForecast" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsEditOppLineItemUnitPrice" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsEditPublicDocuments" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsEditReadonlyFields" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsEditReports" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsEditTask" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsIPRestrictRequests" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsImportLeads" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsInstallMultiforce" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageCallCenters" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageCases" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageCategories" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageCssUsers" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageDashboards" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageLeads" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageSelfService" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageSolutions" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageTerritories" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsManageUsers" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsModifyAllData" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsPasswordNeverExpires" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsPublishMultiforce" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsRunReports" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsSendSitRequests" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsSolutionImport" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsTransferAnyEntity" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsTransferAnyLead" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsUseTeamReassignWizards" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsViewAllData" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PermissionsViewSetup" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="SystemModstamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Users" type="{urn:enterprise.soap.sforce.com}QueryResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Profile", propOrder = {
    "createdBy",
    "createdById",
    "createdDate",
    "description",
    "lastModifiedBy",
    "lastModifiedById",
    "lastModifiedDate",
    "licenseType",
    "name",
    "permissionsApiUserOnly",
    "permissionsAuthorApex",
    "permissionsConvertLeads",
    "permissionsCreateMultiforce",
    "permissionsCustomizeApplication",
    "permissionsDisableNotifications",
    "permissionsEditActivatedOrders",
    "permissionsEditEvent",
    "permissionsEditForecast",
    "permissionsEditOppLineItemUnitPrice",
    "permissionsEditPublicDocuments",
    "permissionsEditReadonlyFields",
    "permissionsEditReports",
    "permissionsEditTask",
    "permissionsIPRestrictRequests",
    "permissionsImportLeads",
    "permissionsInstallMultiforce",
    "permissionsManageCallCenters",
    "permissionsManageCases",
    "permissionsManageCategories",
    "permissionsManageCssUsers",
    "permissionsManageDashboards",
    "permissionsManageLeads",
    "permissionsManageSelfService",
    "permissionsManageSolutions",
    "permissionsManageTerritories",
    "permissionsManageUsers",
    "permissionsModifyAllData",
    "permissionsPasswordNeverExpires",
    "permissionsPublishMultiforce",
    "permissionsRunReports",
    "permissionsSendSitRequests",
    "permissionsSolutionImport",
    "permissionsTransferAnyEntity",
    "permissionsTransferAnyLead",
    "permissionsUseTeamReassignWizards",
    "permissionsViewAllData",
    "permissionsViewSetup",
    "systemModstamp",
    "users"
})
public class Profile
    extends SObject
{

    @XmlElementRef(name = "CreatedBy", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<User> createdBy;
    @XmlElementRef(name = "CreatedById", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> createdById;
    @XmlElementRef(name = "CreatedDate", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> createdDate;
    @XmlElementRef(name = "Description", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> description;
    @XmlElementRef(name = "LastModifiedBy", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<User> lastModifiedBy;
    @XmlElementRef(name = "LastModifiedById", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> lastModifiedById;
    @XmlElementRef(name = "LastModifiedDate", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> lastModifiedDate;
    @XmlElementRef(name = "LicenseType", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> licenseType;
    @XmlElementRef(name = "Name", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "PermissionsApiUserOnly", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsApiUserOnly;
    @XmlElementRef(name = "PermissionsAuthorApex", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsAuthorApex;
    @XmlElementRef(name = "PermissionsConvertLeads", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsConvertLeads;
    @XmlElementRef(name = "PermissionsCreateMultiforce", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsCreateMultiforce;
    @XmlElementRef(name = "PermissionsCustomizeApplication", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsCustomizeApplication;
    @XmlElementRef(name = "PermissionsDisableNotifications", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsDisableNotifications;
    @XmlElementRef(name = "PermissionsEditActivatedOrders", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsEditActivatedOrders;
    @XmlElementRef(name = "PermissionsEditEvent", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsEditEvent;
    @XmlElementRef(name = "PermissionsEditForecast", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsEditForecast;
    @XmlElementRef(name = "PermissionsEditOppLineItemUnitPrice", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsEditOppLineItemUnitPrice;
    @XmlElementRef(name = "PermissionsEditPublicDocuments", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsEditPublicDocuments;
    @XmlElementRef(name = "PermissionsEditReadonlyFields", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsEditReadonlyFields;
    @XmlElementRef(name = "PermissionsEditReports", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsEditReports;
    @XmlElementRef(name = "PermissionsEditTask", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsEditTask;
    @XmlElementRef(name = "PermissionsIPRestrictRequests", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsIPRestrictRequests;
    @XmlElementRef(name = "PermissionsImportLeads", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsImportLeads;
    @XmlElementRef(name = "PermissionsInstallMultiforce", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsInstallMultiforce;
    @XmlElementRef(name = "PermissionsManageCallCenters", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageCallCenters;
    @XmlElementRef(name = "PermissionsManageCases", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageCases;
    @XmlElementRef(name = "PermissionsManageCategories", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageCategories;
    @XmlElementRef(name = "PermissionsManageCssUsers", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageCssUsers;
    @XmlElementRef(name = "PermissionsManageDashboards", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageDashboards;
    @XmlElementRef(name = "PermissionsManageLeads", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageLeads;
    @XmlElementRef(name = "PermissionsManageSelfService", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageSelfService;
    @XmlElementRef(name = "PermissionsManageSolutions", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageSolutions;
    @XmlElementRef(name = "PermissionsManageTerritories", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageTerritories;
    @XmlElementRef(name = "PermissionsManageUsers", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsManageUsers;
    @XmlElementRef(name = "PermissionsModifyAllData", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsModifyAllData;
    @XmlElementRef(name = "PermissionsPasswordNeverExpires", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsPasswordNeverExpires;
    @XmlElementRef(name = "PermissionsPublishMultiforce", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsPublishMultiforce;
    @XmlElementRef(name = "PermissionsRunReports", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsRunReports;
    @XmlElementRef(name = "PermissionsSendSitRequests", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsSendSitRequests;
    @XmlElementRef(name = "PermissionsSolutionImport", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsSolutionImport;
    @XmlElementRef(name = "PermissionsTransferAnyEntity", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsTransferAnyEntity;
    @XmlElementRef(name = "PermissionsTransferAnyLead", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsTransferAnyLead;
    @XmlElementRef(name = "PermissionsUseTeamReassignWizards", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsUseTeamReassignWizards;
    @XmlElementRef(name = "PermissionsViewAllData", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsViewAllData;
    @XmlElementRef(name = "PermissionsViewSetup", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> permissionsViewSetup;
    @XmlElementRef(name = "SystemModstamp", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> systemModstamp;
    @XmlElementRef(name = "Users", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<QueryResult> users;

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link User }{@code >}
     *     
     */
    public JAXBElement<User> getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link User }{@code >}
     *     
     */
    public void setCreatedBy(JAXBElement<User> value) {
        this.createdBy = ((JAXBElement<User> ) value);
    }

    /**
     * Gets the value of the createdById property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCreatedById() {
        return createdById;
    }

    /**
     * Sets the value of the createdById property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCreatedById(JAXBElement<String> value) {
        this.createdById = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the createdDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the value of the createdDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setCreatedDate(JAXBElement<XMLGregorianCalendar> value) {
        this.createdDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescription(JAXBElement<String> value) {
        this.description = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the lastModifiedBy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link User }{@code >}
     *     
     */
    public JAXBElement<User> getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Sets the value of the lastModifiedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link User }{@code >}
     *     
     */
    public void setLastModifiedBy(JAXBElement<User> value) {
        this.lastModifiedBy = ((JAXBElement<User> ) value);
    }

    /**
     * Gets the value of the lastModifiedById property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLastModifiedById() {
        return lastModifiedById;
    }

    /**
     * Sets the value of the lastModifiedById property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLastModifiedById(JAXBElement<String> value) {
        this.lastModifiedById = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the lastModifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the value of the lastModifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setLastModifiedDate(JAXBElement<XMLGregorianCalendar> value) {
        this.lastModifiedDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the licenseType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLicenseType() {
        return licenseType;
    }

    /**
     * Sets the value of the licenseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLicenseType(JAXBElement<String> value) {
        this.licenseType = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(JAXBElement<String> value) {
        this.name = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the permissionsApiUserOnly property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsApiUserOnly() {
        return permissionsApiUserOnly;
    }

    /**
     * Sets the value of the permissionsApiUserOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsApiUserOnly(JAXBElement<Boolean> value) {
        this.permissionsApiUserOnly = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsAuthorApex property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsAuthorApex() {
        return permissionsAuthorApex;
    }

    /**
     * Sets the value of the permissionsAuthorApex property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsAuthorApex(JAXBElement<Boolean> value) {
        this.permissionsAuthorApex = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsConvertLeads property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsConvertLeads() {
        return permissionsConvertLeads;
    }

    /**
     * Sets the value of the permissionsConvertLeads property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsConvertLeads(JAXBElement<Boolean> value) {
        this.permissionsConvertLeads = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsCreateMultiforce property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsCreateMultiforce() {
        return permissionsCreateMultiforce;
    }

    /**
     * Sets the value of the permissionsCreateMultiforce property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsCreateMultiforce(JAXBElement<Boolean> value) {
        this.permissionsCreateMultiforce = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsCustomizeApplication property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsCustomizeApplication() {
        return permissionsCustomizeApplication;
    }

    /**
     * Sets the value of the permissionsCustomizeApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsCustomizeApplication(JAXBElement<Boolean> value) {
        this.permissionsCustomizeApplication = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsDisableNotifications property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsDisableNotifications() {
        return permissionsDisableNotifications;
    }

    /**
     * Sets the value of the permissionsDisableNotifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsDisableNotifications(JAXBElement<Boolean> value) {
        this.permissionsDisableNotifications = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsEditActivatedOrders property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsEditActivatedOrders() {
        return permissionsEditActivatedOrders;
    }

    /**
     * Sets the value of the permissionsEditActivatedOrders property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsEditActivatedOrders(JAXBElement<Boolean> value) {
        this.permissionsEditActivatedOrders = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsEditEvent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsEditEvent() {
        return permissionsEditEvent;
    }

    /**
     * Sets the value of the permissionsEditEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsEditEvent(JAXBElement<Boolean> value) {
        this.permissionsEditEvent = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsEditForecast property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsEditForecast() {
        return permissionsEditForecast;
    }

    /**
     * Sets the value of the permissionsEditForecast property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsEditForecast(JAXBElement<Boolean> value) {
        this.permissionsEditForecast = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsEditOppLineItemUnitPrice property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsEditOppLineItemUnitPrice() {
        return permissionsEditOppLineItemUnitPrice;
    }

    /**
     * Sets the value of the permissionsEditOppLineItemUnitPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsEditOppLineItemUnitPrice(JAXBElement<Boolean> value) {
        this.permissionsEditOppLineItemUnitPrice = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsEditPublicDocuments property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsEditPublicDocuments() {
        return permissionsEditPublicDocuments;
    }

    /**
     * Sets the value of the permissionsEditPublicDocuments property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsEditPublicDocuments(JAXBElement<Boolean> value) {
        this.permissionsEditPublicDocuments = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsEditReadonlyFields property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsEditReadonlyFields() {
        return permissionsEditReadonlyFields;
    }

    /**
     * Sets the value of the permissionsEditReadonlyFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsEditReadonlyFields(JAXBElement<Boolean> value) {
        this.permissionsEditReadonlyFields = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsEditReports property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsEditReports() {
        return permissionsEditReports;
    }

    /**
     * Sets the value of the permissionsEditReports property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsEditReports(JAXBElement<Boolean> value) {
        this.permissionsEditReports = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsEditTask property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsEditTask() {
        return permissionsEditTask;
    }

    /**
     * Sets the value of the permissionsEditTask property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsEditTask(JAXBElement<Boolean> value) {
        this.permissionsEditTask = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsIPRestrictRequests property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsIPRestrictRequests() {
        return permissionsIPRestrictRequests;
    }

    /**
     * Sets the value of the permissionsIPRestrictRequests property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsIPRestrictRequests(JAXBElement<Boolean> value) {
        this.permissionsIPRestrictRequests = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsImportLeads property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsImportLeads() {
        return permissionsImportLeads;
    }

    /**
     * Sets the value of the permissionsImportLeads property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsImportLeads(JAXBElement<Boolean> value) {
        this.permissionsImportLeads = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsInstallMultiforce property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsInstallMultiforce() {
        return permissionsInstallMultiforce;
    }

    /**
     * Sets the value of the permissionsInstallMultiforce property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsInstallMultiforce(JAXBElement<Boolean> value) {
        this.permissionsInstallMultiforce = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageCallCenters property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageCallCenters() {
        return permissionsManageCallCenters;
    }

    /**
     * Sets the value of the permissionsManageCallCenters property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageCallCenters(JAXBElement<Boolean> value) {
        this.permissionsManageCallCenters = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageCases property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageCases() {
        return permissionsManageCases;
    }

    /**
     * Sets the value of the permissionsManageCases property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageCases(JAXBElement<Boolean> value) {
        this.permissionsManageCases = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageCategories property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageCategories() {
        return permissionsManageCategories;
    }

    /**
     * Sets the value of the permissionsManageCategories property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageCategories(JAXBElement<Boolean> value) {
        this.permissionsManageCategories = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageCssUsers property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageCssUsers() {
        return permissionsManageCssUsers;
    }

    /**
     * Sets the value of the permissionsManageCssUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageCssUsers(JAXBElement<Boolean> value) {
        this.permissionsManageCssUsers = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageDashboards property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageDashboards() {
        return permissionsManageDashboards;
    }

    /**
     * Sets the value of the permissionsManageDashboards property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageDashboards(JAXBElement<Boolean> value) {
        this.permissionsManageDashboards = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageLeads property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageLeads() {
        return permissionsManageLeads;
    }

    /**
     * Sets the value of the permissionsManageLeads property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageLeads(JAXBElement<Boolean> value) {
        this.permissionsManageLeads = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageSelfService property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageSelfService() {
        return permissionsManageSelfService;
    }

    /**
     * Sets the value of the permissionsManageSelfService property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageSelfService(JAXBElement<Boolean> value) {
        this.permissionsManageSelfService = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageSolutions property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageSolutions() {
        return permissionsManageSolutions;
    }

    /**
     * Sets the value of the permissionsManageSolutions property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageSolutions(JAXBElement<Boolean> value) {
        this.permissionsManageSolutions = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageTerritories property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageTerritories() {
        return permissionsManageTerritories;
    }

    /**
     * Sets the value of the permissionsManageTerritories property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageTerritories(JAXBElement<Boolean> value) {
        this.permissionsManageTerritories = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsManageUsers property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsManageUsers() {
        return permissionsManageUsers;
    }

    /**
     * Sets the value of the permissionsManageUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsManageUsers(JAXBElement<Boolean> value) {
        this.permissionsManageUsers = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsModifyAllData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsModifyAllData() {
        return permissionsModifyAllData;
    }

    /**
     * Sets the value of the permissionsModifyAllData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsModifyAllData(JAXBElement<Boolean> value) {
        this.permissionsModifyAllData = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsPasswordNeverExpires property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsPasswordNeverExpires() {
        return permissionsPasswordNeverExpires;
    }

    /**
     * Sets the value of the permissionsPasswordNeverExpires property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsPasswordNeverExpires(JAXBElement<Boolean> value) {
        this.permissionsPasswordNeverExpires = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsPublishMultiforce property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsPublishMultiforce() {
        return permissionsPublishMultiforce;
    }

    /**
     * Sets the value of the permissionsPublishMultiforce property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsPublishMultiforce(JAXBElement<Boolean> value) {
        this.permissionsPublishMultiforce = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsRunReports property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsRunReports() {
        return permissionsRunReports;
    }

    /**
     * Sets the value of the permissionsRunReports property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsRunReports(JAXBElement<Boolean> value) {
        this.permissionsRunReports = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsSendSitRequests property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsSendSitRequests() {
        return permissionsSendSitRequests;
    }

    /**
     * Sets the value of the permissionsSendSitRequests property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsSendSitRequests(JAXBElement<Boolean> value) {
        this.permissionsSendSitRequests = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsSolutionImport property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsSolutionImport() {
        return permissionsSolutionImport;
    }

    /**
     * Sets the value of the permissionsSolutionImport property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsSolutionImport(JAXBElement<Boolean> value) {
        this.permissionsSolutionImport = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsTransferAnyEntity property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsTransferAnyEntity() {
        return permissionsTransferAnyEntity;
    }

    /**
     * Sets the value of the permissionsTransferAnyEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsTransferAnyEntity(JAXBElement<Boolean> value) {
        this.permissionsTransferAnyEntity = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsTransferAnyLead property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsTransferAnyLead() {
        return permissionsTransferAnyLead;
    }

    /**
     * Sets the value of the permissionsTransferAnyLead property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsTransferAnyLead(JAXBElement<Boolean> value) {
        this.permissionsTransferAnyLead = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsUseTeamReassignWizards property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsUseTeamReassignWizards() {
        return permissionsUseTeamReassignWizards;
    }

    /**
     * Sets the value of the permissionsUseTeamReassignWizards property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsUseTeamReassignWizards(JAXBElement<Boolean> value) {
        this.permissionsUseTeamReassignWizards = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsViewAllData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsViewAllData() {
        return permissionsViewAllData;
    }

    /**
     * Sets the value of the permissionsViewAllData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsViewAllData(JAXBElement<Boolean> value) {
        this.permissionsViewAllData = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the permissionsViewSetup property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getPermissionsViewSetup() {
        return permissionsViewSetup;
    }

    /**
     * Sets the value of the permissionsViewSetup property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setPermissionsViewSetup(JAXBElement<Boolean> value) {
        this.permissionsViewSetup = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the systemModstamp property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getSystemModstamp() {
        return systemModstamp;
    }

    /**
     * Sets the value of the systemModstamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setSystemModstamp(JAXBElement<XMLGregorianCalendar> value) {
        this.systemModstamp = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the users property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QueryResult }{@code >}
     *     
     */
    public JAXBElement<QueryResult> getUsers() {
        return users;
    }

    /**
     * Sets the value of the users property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QueryResult }{@code >}
     *     
     */
    public void setUsers(JAXBElement<QueryResult> value) {
        this.users = ((JAXBElement<QueryResult> ) value);
    }

}
