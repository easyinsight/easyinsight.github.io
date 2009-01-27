
package com.sforce.soap.enterprise.sobject;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import com.sforce.soap.enterprise.QueryResult;


/**
 * <p>Java class for ApexPackageIdentifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApexPackageIdentifier">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:sobject.enterprise.soap.sforce.com}sObject">
 *       &lt;sequence>
 *         &lt;element name="ApexPackage" type="{urn:sobject.enterprise.soap.sforce.com}ApexPackage" minOccurs="0"/>
 *         &lt;element name="ApexPackageId" type="{urn:enterprise.soap.sforce.com}ID" minOccurs="0"/>
 *         &lt;element name="Arguments" type="{urn:enterprise.soap.sforce.com}QueryResult" minOccurs="0"/>
 *         &lt;element name="ColumnNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IdentifierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdentifierType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LineNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="OptionsFinalVariable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="OptionsPrivateIdentifier" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="OptionsPublicIdentifier" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="OptionsSystemDefined" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="OptionsTestMethod" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="OptionsWebService" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApexPackageIdentifier", propOrder = {
    "apexPackage",
    "apexPackageId",
    "arguments",
    "columnNumber",
    "identifierName",
    "identifierType",
    "lineNumber",
    "optionsFinalVariable",
    "optionsPrivateIdentifier",
    "optionsPublicIdentifier",
    "optionsSystemDefined",
    "optionsTestMethod",
    "optionsWebService"
})
public class ApexPackageIdentifier
    extends SObject
{

    @XmlElementRef(name = "ApexPackage", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<ApexPackage> apexPackage;
    @XmlElementRef(name = "ApexPackageId", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> apexPackageId;
    @XmlElementRef(name = "Arguments", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<QueryResult> arguments;
    @XmlElementRef(name = "ColumnNumber", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Integer> columnNumber;
    @XmlElementRef(name = "IdentifierName", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> identifierName;
    @XmlElementRef(name = "IdentifierType", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> identifierType;
    @XmlElementRef(name = "LineNumber", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Integer> lineNumber;
    @XmlElementRef(name = "OptionsFinalVariable", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> optionsFinalVariable;
    @XmlElementRef(name = "OptionsPrivateIdentifier", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> optionsPrivateIdentifier;
    @XmlElementRef(name = "OptionsPublicIdentifier", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> optionsPublicIdentifier;
    @XmlElementRef(name = "OptionsSystemDefined", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> optionsSystemDefined;
    @XmlElementRef(name = "OptionsTestMethod", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> optionsTestMethod;
    @XmlElementRef(name = "OptionsWebService", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> optionsWebService;

    /**
     * Gets the value of the apexPackage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ApexPackage }{@code >}
     *     
     */
    public JAXBElement<ApexPackage> getApexPackage() {
        return apexPackage;
    }

    /**
     * Sets the value of the apexPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ApexPackage }{@code >}
     *     
     */
    public void setApexPackage(JAXBElement<ApexPackage> value) {
        this.apexPackage = ((JAXBElement<ApexPackage> ) value);
    }

    /**
     * Gets the value of the apexPackageId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getApexPackageId() {
        return apexPackageId;
    }

    /**
     * Sets the value of the apexPackageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setApexPackageId(JAXBElement<String> value) {
        this.apexPackageId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the arguments property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QueryResult }{@code >}
     *     
     */
    public JAXBElement<QueryResult> getArguments() {
        return arguments;
    }

    /**
     * Sets the value of the arguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QueryResult }{@code >}
     *     
     */
    public void setArguments(JAXBElement<QueryResult> value) {
        this.arguments = ((JAXBElement<QueryResult> ) value);
    }

    /**
     * Gets the value of the columnNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getColumnNumber() {
        return columnNumber;
    }

    /**
     * Sets the value of the columnNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setColumnNumber(JAXBElement<Integer> value) {
        this.columnNumber = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the identifierName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdentifierName() {
        return identifierName;
    }

    /**
     * Sets the value of the identifierName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdentifierName(JAXBElement<String> value) {
        this.identifierName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the identifierType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdentifierType() {
        return identifierType;
    }

    /**
     * Sets the value of the identifierType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdentifierType(JAXBElement<String> value) {
        this.identifierType = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the lineNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the value of the lineNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setLineNumber(JAXBElement<Integer> value) {
        this.lineNumber = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the optionsFinalVariable property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getOptionsFinalVariable() {
        return optionsFinalVariable;
    }

    /**
     * Sets the value of the optionsFinalVariable property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setOptionsFinalVariable(JAXBElement<Boolean> value) {
        this.optionsFinalVariable = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the optionsPrivateIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getOptionsPrivateIdentifier() {
        return optionsPrivateIdentifier;
    }

    /**
     * Sets the value of the optionsPrivateIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setOptionsPrivateIdentifier(JAXBElement<Boolean> value) {
        this.optionsPrivateIdentifier = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the optionsPublicIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getOptionsPublicIdentifier() {
        return optionsPublicIdentifier;
    }

    /**
     * Sets the value of the optionsPublicIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setOptionsPublicIdentifier(JAXBElement<Boolean> value) {
        this.optionsPublicIdentifier = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the optionsSystemDefined property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getOptionsSystemDefined() {
        return optionsSystemDefined;
    }

    /**
     * Sets the value of the optionsSystemDefined property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setOptionsSystemDefined(JAXBElement<Boolean> value) {
        this.optionsSystemDefined = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the optionsTestMethod property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getOptionsTestMethod() {
        return optionsTestMethod;
    }

    /**
     * Sets the value of the optionsTestMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setOptionsTestMethod(JAXBElement<Boolean> value) {
        this.optionsTestMethod = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the optionsWebService property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getOptionsWebService() {
        return optionsWebService;
    }

    /**
     * Sets the value of the optionsWebService property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setOptionsWebService(JAXBElement<Boolean> value) {
        this.optionsWebService = ((JAXBElement<Boolean> ) value);
    }

}
