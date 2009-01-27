/**
 * RetryTransactionRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class RetryTransactionRequest  implements java.io.Serializable {
    private java.lang.String originalTransactionId;

    public RetryTransactionRequest() {
    }

    public RetryTransactionRequest(
           java.lang.String originalTransactionId) {
           this.originalTransactionId = originalTransactionId;
    }


    /**
     * Gets the originalTransactionId value for this RetryTransactionRequest.
     * 
     * @return originalTransactionId
     */
    public java.lang.String getOriginalTransactionId() {
        return originalTransactionId;
    }


    /**
     * Sets the originalTransactionId value for this RetryTransactionRequest.
     * 
     * @param originalTransactionId
     */
    public void setOriginalTransactionId(java.lang.String originalTransactionId) {
        this.originalTransactionId = originalTransactionId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetryTransactionRequest)) return false;
        RetryTransactionRequest other = (RetryTransactionRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.originalTransactionId==null && other.getOriginalTransactionId()==null) || 
             (this.originalTransactionId!=null &&
              this.originalTransactionId.equals(other.getOriginalTransactionId())));
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
        if (getOriginalTransactionId() != null) {
            _hashCode += getOriginalTransactionId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetryTransactionRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RetryTransactionRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originalTransactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OriginalTransactionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
