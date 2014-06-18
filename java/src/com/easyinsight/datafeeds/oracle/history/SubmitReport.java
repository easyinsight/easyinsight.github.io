
package com.easyinsight.datafeeds.oracle.history;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="submitReportData" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="appParams" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "submitReportData",
    "appParams"
})
@XmlRootElement(name = "submitReport")
public class SubmitReport {

    @XmlElement(required = true)
    protected byte[] submitReportData;
    @XmlElement(required = true)
    protected String appParams;

    /**
     * Gets the value of the submitReportData property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSubmitReportData() {
        return submitReportData;
    }

    /**
     * Sets the value of the submitReportData property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSubmitReportData(byte[] value) {
        this.submitReportData = value;
    }

    /**
     * Gets the value of the appParams property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppParams() {
        return appParams;
    }

    /**
     * Sets the value of the appParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppParams(String value) {
        this.appParams = value;
    }

}
