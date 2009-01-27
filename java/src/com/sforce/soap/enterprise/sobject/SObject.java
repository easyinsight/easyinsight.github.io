
package com.sforce.soap.enterprise.sobject;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sObject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fieldsToNull" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Id" type="{urn:enterprise.soap.sforce.com}ID" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sObject", propOrder = {
    "fieldsToNull",
    "id"
})
@XmlSeeAlso({
    CaseShare.class,
    Lead.class,
    OpportunityContactRole.class,
    OpportunityCompetitor.class,
    Organization.class,
    CategoryNode.class,
    SelfServiceUser.class,
    ProcessInstanceWorkitem.class,
    NoteAndAttachment.class,
    CaseContactRole.class,
    PricebookEntry.class,
    AccountShare.class,
    ApexPackageIdentifier.class,
    CaseSolution.class,
    OpportunityStage.class,
    Asset.class,
    Group.class,
    Opportunity.class,
    Campaign.class,
    BusinessHours.class,
    FiscalYearSettings.class,
    ApexPackage.class,
    CampaignMember.class,
    AccountTeamMember.class,
    DocumentAttachmentMap.class,
    Note.class,
    Event.class,
    Approval.class,
    Solution.class,
    TestShare.class,
    ProcessInstanceStep.class,
    Document.class,
    Task.class,
    OpportunityPartner.class,
    BrandTemplate.class,
    AccountContactRole.class,
    UserRole.class,
    UserAccountTeamMember.class,
    OpportunityLineItem.class,
    EmailTemplate.class,
    Profile.class,
    QueueSobject.class,
    TaskStatus.class,
    Contact.class,
    ContractContactRole.class,
    SolutionStatus.class,
    Partner.class,
    Folder.class,
    CampaignMemberStatus.class,
    OpportunityShare.class,
    ApexPackageIdentifierArgument.class,
    CategoryData.class,
    GroupMember.class,
    AccountPartner.class,
    Product2 .class,
    WebLink.class,
    LeadHistory.class,
    Attachment.class,
    EmailStatus.class,
    ContractHistory.class,
    OpenActivity.class,
    LeadStatus.class,
    PartnerRole.class,
    Case.class,
    TestC.class,
    Name.class,
    EventAttendee.class,
    AssignmentRule.class,
    Scontrol.class,
    SolutionHistory.class,
    Account.class,
    TaskPriority.class,
    MailmergeTemplate.class,
    ApexTrigger.class,
    OpportunityHistory.class,
    RecordType.class,
    ActivityHistory.class,
    Pricebook2 .class,
    LeadShare.class,
    User.class,
    ProcessInstanceHistory.class,
    OpportunityTeamMember.class,
    CaseComment.class,
    Period.class,
    UserTeamMember.class,
    Contract.class,
    ProcessInstance.class,
    ContractStatus.class,
    CaseHistory.class,
    BusinessProcess.class,
    CaseStatus.class
})
public class SObject {

    @XmlElement(nillable = true)
    protected List<String> fieldsToNull;
    @XmlElementRef(name = "Id", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> id;

    /**
     * Gets the value of the fieldsToNull property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldsToNull property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldsToNull().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFieldsToNull() {
        if (fieldsToNull == null) {
            fieldsToNull = new ArrayList<String>();
        }
        return this.fieldsToNull;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setId(JAXBElement<String> value) {
        this.id = ((JAXBElement<String> ) value);
    }

}
