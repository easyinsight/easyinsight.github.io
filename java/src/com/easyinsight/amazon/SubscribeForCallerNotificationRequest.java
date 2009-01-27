/**
 * SubscribeForCallerNotificationRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class SubscribeForCallerNotificationRequest  implements java.io.Serializable {
    private java.lang.String notificationOperationName;

    private org.apache.axis.types.URI webServiceAPIURL;

    public SubscribeForCallerNotificationRequest() {
    }

    public SubscribeForCallerNotificationRequest(
           java.lang.String notificationOperationName,
           org.apache.axis.types.URI webServiceAPIURL) {
           this.notificationOperationName = notificationOperationName;
           this.webServiceAPIURL = webServiceAPIURL;
    }


    /**
     * Gets the notificationOperationName value for this SubscribeForCallerNotificationRequest.
     * 
     * @return notificationOperationName
     */
    public java.lang.String getNotificationOperationName() {
        return notificationOperationName;
    }


    /**
     * Sets the notificationOperationName value for this SubscribeForCallerNotificationRequest.
     * 
     * @param notificationOperationName
     */
    public void setNotificationOperationName(java.lang.String notificationOperationName) {
        this.notificationOperationName = notificationOperationName;
    }


    /**
     * Gets the webServiceAPIURL value for this SubscribeForCallerNotificationRequest.
     * 
     * @return webServiceAPIURL
     */
    public org.apache.axis.types.URI getWebServiceAPIURL() {
        return webServiceAPIURL;
    }


    /**
     * Sets the webServiceAPIURL value for this SubscribeForCallerNotificationRequest.
     * 
     * @param webServiceAPIURL
     */
    public void setWebServiceAPIURL(org.apache.axis.types.URI webServiceAPIURL) {
        this.webServiceAPIURL = webServiceAPIURL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubscribeForCallerNotificationRequest)) return false;
        SubscribeForCallerNotificationRequest other = (SubscribeForCallerNotificationRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.notificationOperationName==null && other.getNotificationOperationName()==null) || 
             (this.notificationOperationName!=null &&
              this.notificationOperationName.equals(other.getNotificationOperationName()))) &&
            ((this.webServiceAPIURL==null && other.getWebServiceAPIURL()==null) || 
             (this.webServiceAPIURL!=null &&
              this.webServiceAPIURL.equals(other.getWebServiceAPIURL())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getNotificationOperationName() != null) {
            _hashCode += getNotificationOperationName().hashCode();
        }
        if (getWebServiceAPIURL() != null) {
            _hashCode += getWebServiceAPIURL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SubscribeForCallerNotificationRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SubscribeForCallerNotificationRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificationOperationName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NotificationOperationName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("webServiceAPIURL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "WebServiceAPIURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
