
package com.sforce.soap.enterprise.sobject;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApexPackageIdentifierArgument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApexPackageIdentifierArgument">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:sobject.enterprise.soap.sforce.com}sObject">
 *       &lt;sequence>
 *         &lt;element name="ApexPackageId" type="{urn:enterprise.soap.sforce.com}ID" minOccurs="0"/>
 *         &lt;element name="ApexPackageIdentifier" type="{urn:sobject.enterprise.soap.sforce.com}ApexPackageIdentifier" minOccurs="0"/>
 *         &lt;element name="ApexPackageIdentifierId" type="{urn:enterprise.soap.sforce.com}ID" minOccurs="0"/>
 *         &lt;element name="ArgumentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ArgumentNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ArgumentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CollectionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValueType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApexPackageIdentifierArgument", propOrder = {
    "apexPackageId",
    "apexPackageIdentifier",
    "apexPackageIdentifierId",
    "argumentName",
    "argumentNumber",
    "argumentType",
    "collectionType",
    "valueType"
})
public class ApexPackageIdentifierArgument
    extends SObject
{

    @XmlElementRef(name = "ApexPackageId", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> apexPackageId;
    @XmlElementRef(name = "ApexPackageIdentifier", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<ApexPackageIdentifier> apexPackageIdentifier;
    @XmlElementRef(name = "ApexPackageIdentifierId", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> apexPackageIdentifierId;
    @XmlElementRef(name = "ArgumentName", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> argumentName;
    @XmlElementRef(name = "ArgumentNumber", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<Integer> argumentNumber;
    @XmlElementRef(name = "ArgumentType", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> argumentType;
    @XmlElementRef(name = "CollectionType", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> collectionType;
    @XmlElementRef(name = "ValueType", namespace = "urn:sobject.enterprise.soap.sforce.com", type = JAXBElement.class)
    protected JAXBElement<String> valueType;

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
     * Gets the value of the apexPackageIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ApexPackageIdentifier }{@code >}
     *     
     */
    public JAXBElement<ApexPackageIdentifier> getApexPackageIdentifier() {
        return apexPackageIdentifier;
    }

    /**
     * Sets the value of the apexPackageIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ApexPackageIdentifier }{@code >}
     *     
     */
    public void setApexPackageIdentifier(JAXBElement<ApexPackageIdentifier> value) {
        this.apexPackageIdentifier = ((JAXBElement<ApexPackageIdentifier> ) value);
    }

    /**
     * Gets the value of the apexPackageIdentifierId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getApexPackageIdentifierId() {
        return apexPackageIdentifierId;
    }

    /**
     * Sets the value of the apexPackageIdentifierId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setApexPackageIdentifierId(JAXBElement<String> value) {
        this.apexPackageIdentifierId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the argumentName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getArgumentName() {
        return argumentName;
    }

    /**
     * Sets the value of the argumentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setArgumentName(JAXBElement<String> value) {
        this.argumentName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the argumentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getArgumentNumber() {
        return argumentNumber;
    }

    /**
     * Sets the value of the argumentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setArgumentNumber(JAXBElement<Integer> value) {
        this.argumentNumber = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the argumentType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getArgumentType() {
        return argumentType;
    }

    /**
     * Sets the value of the argumentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setArgumentType(JAXBElement<String> value) {
        this.argumentType = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the collectionType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCollectionType() {
        return collectionType;
    }

    /**
     * Sets the value of the collectionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCollectionType(JAXBElement<String> value) {
        this.collectionType = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the valueType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getValueType() {
        return valueType;
    }

    /**
     * Sets the value of the valueType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setValueType(JAXBElement<String> value) {
        this.valueType = ((JAXBElement<String> ) value);
    }

}
