
package com.easyinsight.datafeeds.netsuite.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SolutionSearch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SolutionSearch">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:core_2014_1.platform.webservices.netsuite.com}SearchRecord">
 *       &lt;sequence>
 *         &lt;element name="basic" type="{urn:common_2014_1.platform.webservices.netsuite.com}SolutionSearchBasic" minOccurs="0"/>
 *         &lt;element name="caseJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}SupportCaseSearchBasic" minOccurs="0"/>
 *         &lt;element name="relatedSolutionJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}SolutionSearchBasic" minOccurs="0"/>
 *         &lt;element name="topicJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}TopicSearchBasic" minOccurs="0"/>
 *         &lt;element name="userJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}EmployeeSearchBasic" minOccurs="0"/>
 *         &lt;element name="userNotesJoin" type="{urn:common_2014_1.platform.webservices.netsuite.com}NoteSearchBasic" minOccurs="0"/>
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
@XmlType(name = "SolutionSearch", namespace = "urn:support_2014_1.lists.webservices.netsuite.com", propOrder = {
    "basic",
    "caseJoin",
    "relatedSolutionJoin",
    "topicJoin",
    "userJoin",
    "userNotesJoin",
    "customSearchJoin"
})
public class SolutionSearch
    extends SearchRecord
{

    protected SolutionSearchBasic basic;
    protected SupportCaseSearchBasic caseJoin;
    protected SolutionSearchBasic relatedSolutionJoin;
    protected TopicSearchBasic topicJoin;
    protected EmployeeSearchBasic userJoin;
    protected NoteSearchBasic userNotesJoin;
    protected List<CustomSearchJoin> customSearchJoin;

    /**
     * Gets the value of the basic property.
     * 
     * @return
     *     possible object is
     *     {@link SolutionSearchBasic }
     *     
     */
    public SolutionSearchBasic getBasic() {
        return basic;
    }

    /**
     * Sets the value of the basic property.
     * 
     * @param value
     *     allowed object is
     *     {@link SolutionSearchBasic }
     *     
     */
    public void setBasic(SolutionSearchBasic value) {
        this.basic = value;
    }

    /**
     * Gets the value of the caseJoin property.
     * 
     * @return
     *     possible object is
     *     {@link SupportCaseSearchBasic }
     *     
     */
    public SupportCaseSearchBasic getCaseJoin() {
        return caseJoin;
    }

    /**
     * Sets the value of the caseJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupportCaseSearchBasic }
     *     
     */
    public void setCaseJoin(SupportCaseSearchBasic value) {
        this.caseJoin = value;
    }

    /**
     * Gets the value of the relatedSolutionJoin property.
     * 
     * @return
     *     possible object is
     *     {@link SolutionSearchBasic }
     *     
     */
    public SolutionSearchBasic getRelatedSolutionJoin() {
        return relatedSolutionJoin;
    }

    /**
     * Sets the value of the relatedSolutionJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link SolutionSearchBasic }
     *     
     */
    public void setRelatedSolutionJoin(SolutionSearchBasic value) {
        this.relatedSolutionJoin = value;
    }

    /**
     * Gets the value of the topicJoin property.
     * 
     * @return
     *     possible object is
     *     {@link TopicSearchBasic }
     *     
     */
    public TopicSearchBasic getTopicJoin() {
        return topicJoin;
    }

    /**
     * Sets the value of the topicJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link TopicSearchBasic }
     *     
     */
    public void setTopicJoin(TopicSearchBasic value) {
        this.topicJoin = value;
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
     * Gets the value of the userNotesJoin property.
     * 
     * @return
     *     possible object is
     *     {@link NoteSearchBasic }
     *     
     */
    public NoteSearchBasic getUserNotesJoin() {
        return userNotesJoin;
    }

    /**
     * Sets the value of the userNotesJoin property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoteSearchBasic }
     *     
     */
    public void setUserNotesJoin(NoteSearchBasic value) {
        this.userNotesJoin = value;
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
